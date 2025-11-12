<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/views/common/header.jsp" %>

<style>
:root {
    --primary: #141a46;
    --accent: #ec8b5e;
    --yellow: #ffde59;
    --success: #28a745;
    --warning: #ffc107;
    --danger: #dc3545;
    --info: #17a2b8;
}

.bookings-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 30px 15px;
}

.page-header {
    background: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
    color: white;
    border-radius: 20px;
    padding: 40px;
    margin-bottom: 30px;
    text-align: center;
}

.page-title {
    font-size: 2rem;
    font-weight: 900;
    margin-bottom: 10px;
}

.booking-card {
    background: white;
    border-radius: 15px;
    padding: 25px;
    margin-bottom: 20px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
}

.booking-card:hover {
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.booking-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 2px solid #e9ecef;
}

.booking-status {
    padding: 8px 20px;
    border-radius: 20px;
    font-weight: 700;
    font-size: 0.9rem;
}

/* Booking Status Styles - Different colors for each status */
.booking-status {
    padding: 8px 20px;
    border-radius: 20px;
    font-weight: 700;
    font-size: 0.9rem;
    display: inline-block;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

/* Chờ Xác Nhận - Màu vàng */
.status-PENDING {
    background: var(--yellow);
    color: var(--primary);
    border: 2px solid var(--yellow-dark);
    box-shadow: 0 2px 8px rgba(255, 222, 89, 0.3);
}

/* Đã Xác Nhận - Màu xanh lá */
.status-CONFIRMED {
    background: #d4edda;
    color: #155724;
    border: 2px solid #28a745;
    box-shadow: 0 2px 8px rgba(40, 167, 69, 0.3);
}

/* Hoàn Thành - Màu xanh dương */
.status-COMPLETED {
    background: #d1ecf1;
    color: #0c5460;
    border: 2px solid #17a2b8;
    box-shadow: 0 2px 8px rgba(23, 162, 184, 0.3);
}

/* Đã Hủy - Màu đỏ nhạt */
.status-CANCELLED {
    background: #f8d7da;
    color: #721c24;
    border: 2px solid #dc3545;
    box-shadow: 0 2px 8px rgba(220, 53, 69, 0.3);
}

.booking-info {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
}

.info-item {
    display: flex;
    align-items: center;
    gap: 10px;
}

.info-icon {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background: var(--accent);
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
}

.info-content {
    flex: 1;
}

.info-label {
    font-size: 0.85rem;
    color: #6c757d;
    margin-bottom: 3px;
}

.info-value {
    font-size: 1rem;
    font-weight: 700;
    color: var(--primary);
}

.booking-actions {
    margin-top: 20px;
    display: flex;
    gap: 10px;
    justify-content: flex-end;
}

.btn {
    padding: 10px 25px;
    border-radius: 20px;
    font-weight: 700;
    cursor: pointer;
    transition: all 0.3s ease;
    border: none;
}

.btn-danger {
    background: var(--danger);
    color: white;
}

.btn-danger:hover {
    background: #c82333;
    transform: translateY(-2px);
}

.btn-new {
    background: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
    color: white;
    padding: 15px 35px;
    border-radius: 25px;
    font-weight: 700;
    text-decoration: none;
    display: inline-block;
    margin-bottom: 20px;
    transition: all 0.3s ease;
}

.btn-new:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba(236, 139, 94, 0.3);
}

.empty-state {
    text-align: center;
    padding: 60px 20px;
}

.empty-icon {
    font-size: 5rem;
    color: #dee2e6;
    margin-bottom: 20px;
}

.empty-title {
    font-size: 1.5rem;
    font-weight: 700;
    color: var(--primary);
    margin-bottom: 10px;
}

.empty-text {
    color: #6c757d;
    margin-bottom: 30px;
}
</style>

<div class="bookings-container">
    <!-- Page Header -->
    <div class="page-header">
        <h1 class="page-title">
            <i class="fas fa-calendar-check"></i> Lịch Tập Của Tôi
        </h1>
        <p>Quản lý các buổi tập cá nhân với PT</p>
    </div>

    <!-- New Booking Button -->
    <a href="${pageContext.request.contextPath}/member/schedule" class="btn-new">
        <i class="fas fa-plus-circle"></i> Đặt Lịch Mới
    </a>

    <!-- Bookings List -->
    <div id="bookingsList">
        <!-- Will be loaded via JavaScript -->
    </div>
</div>

<script>
// Store context path in JavaScript variable
const contextPath = '${pageContext.request.contextPath}';

// Load bookings on page load
document.addEventListener('DOMContentLoaded', function() {
    loadBookings();
});

// Load bookings from server
function loadBookings() {
    fetch(contextPath + '/api/schedule/bookings')
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                displayBookings(result.data);
            } else {
                showError('Không thể tải danh sách lịch tập');
            }
        })
        .catch(error => {
            console.error('Error loading bookings:', error);
            showError('Có lỗi xảy ra khi tải danh sách');
        });
}

// Display bookings
function displayBookings(bookings) {
    const container = document.getElementById('bookingsList');
    
    if (bookings.length === 0) {
        container.innerHTML = 
            '<div class="empty-state">' +
                '<div class="empty-icon">' +
                    '<i class="fas fa-calendar-times"></i>' +
                '</div>' +
                '<h3 class="empty-title">Chưa Có Lịch Tập</h3>' +
                '<p class="empty-text">Bạn chưa đặt lịch tập nào. Hãy bắt đầu ngay!</p>' +
                '<a href="' + contextPath + '/member/schedule" class="btn-new">' +
                    '<i class="fas fa-plus-circle"></i> Đặt Lịch Ngay' +
                '</a>' +
            '</div>';
        return;
    }
    
    // Sort by date (upcoming first - lịch gần nhất lên trước)
    bookings.sort((a, b) => {
        const dateA = new Date(a.bookingDate);
        const dateB = new Date(b.bookingDate);
        // Sort ascending: upcoming dates first
        return dateA - dateB;
    });
    
    container.innerHTML = '';
    bookings.forEach(booking => {
        const card = createBookingCard(booking);
        container.appendChild(card);
    });
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Create booking card
function createBookingCard(booking) {
    const card = document.createElement('div');
    card.className = 'booking-card';
    
    const statusClass = 'status-' + (booking.bookingStatus || '');
    const statusText = getStatusText(booking.bookingStatus);
    const canCancel = booking.bookingStatus === 'PENDING' || booking.bookingStatus === 'CONFIRMED';
    
    const trainerName = booking.trainer ? escapeHtml(booking.trainer.name) : 'N/A';
    const gymName = booking.gym ? escapeHtml(booking.gym.gymName || booking.gym.name) : 'N/A';
    const bookingDate = formatDate(booking.bookingDate);
    const timeSlot = booking.timeSlot ? escapeHtml(booking.timeSlot.startTime) + ' - ' + escapeHtml(booking.timeSlot.endTime) : 'N/A';
    const notes = booking.notes ? '<div style="margin-top: 15px; padding: 15px; background: #f8f9fa; border-radius: 10px;"><strong>Ghi chú:</strong> ' + escapeHtml(booking.notes) + '</div>' : '';
    const cancelledReason = booking.cancelledReason ? '<div style="margin-top: 15px; padding: 15px; background: #f8d7da; border-radius: 10px; color: #721c24;"><strong>Lý do hủy:</strong> ' + escapeHtml(booking.cancelledReason) + '</div>' : '';
    const cancelButton = canCancel ? '<div class="booking-actions"><button class="btn btn-danger" onclick="cancelBooking(' + booking.bookingId + ', event)"><i class="fas fa-times-circle"></i> Hủy Lịch</button></div>' : '';
    
    card.innerHTML = 
        '<div class="booking-header">' +
            '<h3 style="margin: 0; color: var(--primary);">' +
                '<i class="fas fa-dumbbell"></i> Buổi Tập PT' +
            '</h3>' +
            '<span class="booking-status ' + statusClass + '">' + escapeHtml(statusText) + '</span>' +
        '</div>' +
        '<div class="booking-info">' +
            '<div class="info-item">' +
                '<div class="info-icon"><i class="fas fa-building"></i></div>' +
                '<div class="info-content">' +
                    '<div class="info-label">Cơ sở</div>' +
                    '<div class="info-value">' + gymName + '</div>' +
                '</div>' +
            '</div>' +
            '<div class="info-item">' +
                '<div class="info-icon"><i class="fas fa-user-tie"></i></div>' +
                '<div class="info-content">' +
                    '<div class="info-label">Huấn luyện viên</div>' +
                    '<div class="info-value">' + trainerName + '</div>' +
                '</div>' +
            '</div>' +
            '<div class="info-item">' +
                '<div class="info-icon"><i class="fas fa-calendar"></i></div>' +
                '<div class="info-content">' +
                    '<div class="info-label">Ngày tập</div>' +
                    '<div class="info-value">' + bookingDate + '</div>' +
                '</div>' +
            '</div>' +
            '<div class="info-item">' +
                '<div class="info-icon"><i class="fas fa-clock"></i></div>' +
                '<div class="info-content">' +
                    '<div class="info-label">Giờ tập</div>' +
                    '<div class="info-value">' + timeSlot + '</div>' +
                '</div>' +
            '</div>' +
        '</div>' +
        notes +
        cancelledReason +
        cancelButton;
    
    return card;
}

// Get status text
function getStatusText(status) {
    const statusMap = {
        'PENDING': 'Chờ Xác Nhận',
        'CONFIRMED': 'Đã Xác Nhận',
        'CANCELLED': 'Đã Hủy',
        'COMPLETED': 'Hoàn Thành'
    };
    return statusMap[status] || status;
}

// Format date
function formatDate(dateStr) {
    const date = new Date(dateStr);
    return date.toLocaleDateString('vi-VN', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// Cancel booking
function cancelBooking(bookingId, event) {
    if (!confirm('Bạn có chắc muốn hủy lịch tập này?\n\n⚠️ Lưu ý: Chỉ có thể hủy lịch trước 24 giờ diễn ra buổi tập.\n\nKhung giờ sẽ được giải phóng để người khác có thể đặt lịch.')) {
        return;
    }
    
    // Disable button and show loading
    const button = (event && event.target) ? event.target.closest('button') : document.querySelector('button[onclick*="cancelBooking(' + bookingId + ')"]');
    if (!button) {
        alert('Không tìm thấy nút hủy lịch');
        return;
    }
    const originalText = button.innerHTML;
    button.disabled = true;
    button.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
    
    fetch(contextPath + '/api/schedule/bookings/' + bookingId + '/cancel', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    })
    .then(response => {
        // Always parse as text first, then try to parse as JSON
        return response.text().then(text => {
            try {
                // Try to parse as JSON
                const jsonData = JSON.parse(text);
                return jsonData;
            } catch (e) {
                // If not JSON, check response status
                if (response.ok) {
                    // Success but not JSON - return success
                    return { success: true, message: 'Hủy lịch thành công!' };
                } else {
                    // Error response - extract message from HTML if possible
                    let errorMsg = 'Không thể hủy lịch. Vui lòng thử lại.';
                    if (text.includes('405') || text.includes('Méthode non autorisée')) {
                        errorMsg = 'Phương thức không được phép. Vui lòng thử lại.';
                    } else if (text.includes('401') || text.includes('đăng nhập')) {
                        errorMsg = 'Vui lòng đăng nhập lại.';
                    } else if (text.length < 500 && text.trim().length > 0) {
                        // If text is short, might be a simple error message
                        errorMsg = text.trim();
                    }
                    return { success: false, message: errorMsg };
                }
            }
        });
    })
    .then(result => {
        if (result && result.success) {
            // Show success message using alert
            alert('Hủy lịch thành công');
            
            // Find and remove the booking card from UI
            const card = button.closest('.booking-card');
            if (card) {
                // Add fade out animation
                card.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
                card.style.opacity = '0';
                card.style.transform = 'translateX(-20px)';
                
                // Remove card after animation
                setTimeout(() => {
                    card.remove();
                    
                    // Check if there are no more bookings
                    const bookingsList = document.getElementById('bookingsList');
                    if (bookingsList && bookingsList.children.length === 0) {
                        bookingsList.innerHTML = 
                            '<div class="empty-state">' +
                                '<div class="empty-icon">' +
                                    '<i class="fas fa-calendar-times"></i>' +
                                '</div>' +
                                '<h3 class="empty-title">Chưa Có Lịch Tập</h3>' +
                                '<p class="empty-text">Bạn chưa đặt lịch tập nào. Hãy bắt đầu ngay!</p>' +
                                '<a href="' + contextPath + '/member/schedule" class="btn-new">' +
                                    '<i class="fas fa-plus-circle"></i> Đặt Lịch Ngay' +
                                '</a>' +
                            '</div>';
                    }
                }, 300);
            } else {
                // Fallback: reload bookings if card not found
                setTimeout(() => {
                    loadBookings();
                }, 500);
            }
        } else {
            const errorMsg = (result && result.message) ? result.message : 'Không thể hủy lịch. Vui lòng thử lại.';
            alert('Hủy lịch không thành công: ' + errorMsg);
            // Re-enable button
            button.disabled = false;
            button.innerHTML = originalText;
        }
    })
    .catch(error => {
        console.error('Error cancelling booking:', error);
        let errorMsg = 'Có lỗi xảy ra khi hủy lịch. Vui lòng thử lại.';
        if (error.message && !error.message.includes('<html') && !error.message.includes('<!doctype')) {
            errorMsg = error.message;
        }
        alert(errorMsg);
        // Re-enable button
        button.disabled = false;
        button.innerHTML = originalText;
    });
}

// Show error
function showError(message) {
    const container = document.getElementById('bookingsList');
    container.innerHTML = 
        '<div style="text-align: center; padding: 40px; color: #dc3545;">' +
            '<i class="fas fa-exclamation-circle" style="font-size: 3rem; margin-bottom: 15px;"></i>' +
            '<h3>' + escapeHtml(message) + '</h3>' +
        '</div>';
}
</script>

<%@ include file="/views/common/footer.jsp" %>
