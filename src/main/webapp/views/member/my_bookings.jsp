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

.status-PENDING {
    background: #fff3cd;
    color: #856404;
}

.status-CONFIRMED {
    background: #d4edda;
    color: #155724;
}

.status-CANCELLED {
    background: #f8d7da;
    color: #721c24;
}

.status-COMPLETED {
    background: #d1ecf1;
    color: #0c5460;
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
// Get context path
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
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">
                    <i class="fas fa-calendar-times"></i>
                </div>
                <h3 class="empty-title">Chưa Có Lịch Tập</h3>
                <p class="empty-text">Bạn chưa đặt lịch tập nào. Hãy bắt đầu ngay!</p>
                <a href="${contextPath}/member/schedule" class="btn-new">
                    <i class="fas fa-plus-circle"></i> Đặt Lịch Ngay
                </a>
            </div>
        `;
        return;
    }
    
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
    
    const statusClass = `status-${booking.bookingStatus}`;
    const statusText = getStatusText(booking.bookingStatus);
    const canCancel = booking.bookingStatus === 'PENDING' || booking.bookingStatus === 'CONFIRMED';
    
    // Build HTML content
    let html = `
        <div class="booking-header">
            <h3 style="margin: 0; color: var(--primary);">
                <i class="fas fa-dumbbell"></i> Buổi Tập PT
            </h3>
            <span class="booking-status ${statusClass}">${escapeHtml(statusText)}</span>
        </div>
        
        <div class="booking-info">
            <div class="info-item">
                <div class="info-icon">
                    <i class="fas fa-user-tie"></i>
                </div>
                <div class="info-content">
                    <div class="info-label">Huấn luyện viên</div>
                    <div class="info-value">${escapeHtml(booking.trainer ? booking.trainer.name : 'N/A')}</div>
                </div>
            </div>
            
            <div class="info-item">
                <div class="info-icon">
                    <i class="fas fa-calendar"></i>
                </div>
                <div class="info-content">
                    <div class="info-label">Ngày tập</div>
                    <div class="info-value">${formatDate(booking.bookingDate)}</div>
                </div>
            </div>
            
            <div class="info-item">
                <div class="info-icon">
                    <i class="fas fa-clock"></i>
                </div>
                <div class="info-content">
                    <div class="info-label">Giờ tập</div>
                    <div class="info-value">
                        ${booking.timeSlot ? escapeHtml(booking.timeSlot.startTime + ' - ' + booking.timeSlot.endTime) : 'N/A'}
                    </div>
                </div>
            </div>
        </div>
    `;
    
    // Add notes if exists
    if (booking.notes && booking.notes.trim()) {
        html += `
            <div style="margin-top: 15px; padding: 15px; background: #f8f9fa; border-radius: 10px;">
                <strong>Ghi chú:</strong> ${escapeHtml(booking.notes)}
            </div>
        `;
    }
    
    // Add cancelled reason if exists
    if (booking.cancelledReason && booking.cancelledReason.trim()) {
        html += `
            <div style="margin-top: 15px; padding: 15px; background: #f8d7da; border-radius: 10px; color: #721c24;">
                <strong>Lý do hủy:</strong> ${escapeHtml(booking.cancelledReason)}
            </div>
        `;
    }
    
    // Add cancel button if applicable
    if (canCancel) {
        html += `
            <div class="booking-actions">
                <button class="btn btn-danger" onclick="cancelBooking(${booking.bookingId})">
                    <i class="fas fa-times-circle"></i> Hủy Lịch
                </button>
            </div>
        `;
    }
    
    card.innerHTML = html;
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
function cancelBooking(bookingId) {
    if (!confirm('Bạn có chắc muốn hủy lịch tập này?\n\nLưu ý: Chỉ có thể hủy trước 24 giờ.')) {
        return;
    }
    
    fetch(contextPath + '/api/schedule/bookings/' + bookingId + '/cancel', {
        method: 'PUT'
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            alert('Hủy lịch thành công!');
            loadBookings(); // Reload list
        } else {
            alert(result.message || 'Không thể hủy lịch');
        }
    })
    .catch(error => {
        console.error('Error cancelling booking:', error);
        alert('Có lỗi xảy ra khi hủy lịch');
    });
}

// Show error
function showError(message) {
    const container = document.getElementById('bookingsList');
    container.innerHTML = `
        <div style="text-align: center; padding: 40px; color: #dc3545;">
            <i class="fas fa-exclamation-circle" style="font-size: 3rem; margin-bottom: 15px;"></i>
            <h3>${message}</h3>
        </div>
    `;
}
</script>

<%@ include file="/views/common/footer.jsp" %>
