package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import model.shop.PaymentStatus;
import service.shop.PaymentService;
import service.shop.PaymentServiceImpl;
import service.shop.VNPayService;

/**
 * VNPay IPN/Notify Servlet
 * Server-to-server callback from VNPay to confirm payment result.
 * - Verifies vnp_SecureHash exactly like VNPay demo (encode values before hashing)
 * - Checks basic order existence and amount if available
 * - Updates payment status and responds JSON per VNPay spec
 *
 * Endpoint: /vnpay-ipn
 */
@WebServlet(name = "VNPayIPNServlet", urlPatterns = {"/vnpay-ipn"})
public class VNPayIPNServlet extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(VNPayIPNServlet.class.getName());

	private transient VNPayService vnPayService;
	private transient PaymentService paymentService;

	@Override
	public void init() throws ServletException {
		super.init();
		this.vnPayService = new VNPayService();
		this.paymentService = new PaymentServiceImpl();
		LOGGER.info("[VNPayIPNServlet] Initialized");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processIpn(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processIpn(request, response);
	}

	/**
	 * Process VNPay IPN callback
	 * Response must be JSON with fields: RspCode, Message
	 */
	private void processIpn(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json; charset=UTF-8");
		Map<String, String> vnpParams = new HashMap<>();

		// Collect parameters (first occurrence only)
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String name = e.nextElement();
			if (!vnpParams.containsKey(name)) {
				String value = request.getParameter(name);
				vnpParams.put(name, value != null ? value.trim() : "");
			}
		}

		try (PrintWriter out = response.getWriter()) {
			// Verify signature using the same logic as return page
			boolean signatureOk = vnPayService.verifySignature(vnpParams);
			if (!signatureOk) {
				out.print("{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}");
				return;
			}

			// Extract required fields
			String txnRef = vnpParams.get("vnp_TxnRef"); // order id
			String responseCode = vnpParams.get("vnp_ResponseCode");

			// Basic validations (you can extend to check exact amount against DB)
			if (txnRef == null || txnRef.isEmpty()) {
				out.print("{\"RspCode\":\"01\",\"Message\":\"Order not Found\"}");
				return;
			}

			// Find and update payment by order
			try {
				Integer orderId = Integer.valueOf(txnRef);

				// TODO: If needed, compare with your order amount here.
				// If mismatch: out.print("{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}"); return;

				// Update payment status of the latest payment record for this order
				java.util.List<model.Payment> payments = paymentService.findPaymentsByOrder(orderId.longValue());
				if (payments == null || payments.isEmpty()) {
					out.print("{\"RspCode\":\"01\",\"Message\":\"Order not Found\"}");
					return;
				}
				// Pick the last payment as the most recent one
				model.Payment latest = payments.get(payments.size() - 1);
				boolean updated;
				if ("00".equals(responseCode)) {
					updated = paymentService.updatePaymentStatus(latest.getPaymentId(), PaymentStatus.PAID, vnpParams.get("vnp_TransactionNo"));
				} else {
					updated = paymentService.updatePaymentStatus(latest.getPaymentId(), PaymentStatus.FAILED, vnpParams.get("vnp_TransactionNo"));
				}
				if (!updated) {
					out.print("{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}");
					return;
				}

				// Everything OK
				out.print("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");
				return;
			} catch (Exception ex) {
				LOGGER.warning("[VNPayIPNServlet] Error updating payment: " + ex.getMessage());
				out.print("{\"RspCode\":\"99\",\"Message\":\"Unknow error\"}");
			}
		}
	}
}


