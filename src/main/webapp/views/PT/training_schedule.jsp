<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Lịch huấn luyện - PT</title>
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
      rel="stylesheet"
    />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap"
      rel="stylesheet"
    />
    <style>
      :root {
        --primary: #141a49;
        --accent: #ec8b5a;
        --success: #28a745;
        --danger: #dc3545;
        --warning: #ffc107;
        --info: #17a2b8;
        --card: #ffffff;
        --shadow: rgba(0, 0, 0, 0.1);
        --border: #e0e0e0;
        --text-muted: #6c757d;
      }
      * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
      }
      body {
        font-family: 'Inter', sans-serif;
        background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
        color: #2c3e50;
        padding: 30px 20px;
        min-height: 100vh;
      }
      .container {
        max-width: 1400px;
        margin: 0 auto;
      }
      .page-header {
        background: linear-gradient(135deg, var(--primary), #1f2961);
        color: #fff;
        padding: 30px 40px;
        border-radius: 20px;
        margin-bottom: 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
      }
      .page-header h1 {
        font-size: 2rem;
        font-weight: 700;
        display: flex;
        align-items: center;
        gap: 15px;
      }
      .btn {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        background: var(--accent);
        color: #fff;
        padding: 12px 24px;
        border: none;
        border-radius: 12px;
        cursor: pointer;
        font-weight: 600;
        transition: all 0.3s ease;
        text-decoration: none;
        font-size: 0.95rem;
      }
      .btn:hover {
        background: #d87548;
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(236, 139, 90, 0.4);
      }
      .btn-danger {
        background: var(--danger);
      }
      .btn-danger:hover {
        background: #c82333;
      }
      .btn-sm {
        padding: 8px 16px;
        font-size: 0.85rem;
      }
      .card {
        background: var(--card);
        padding: 30px;
        border-radius: 20px;
        box-shadow: 0 10px 30px var(--shadow);
        margin-bottom: 30px;
        transition: transform 0.3s ease;
      }
      .card:hover {
        transform: translateY(-2px);
      }
      .section-title {
        color: var(--primary);
        font-weight: 700;
        margin-bottom: 25px;
        font-size: 1.4rem;
        display: flex;
        align-items: center;
        gap: 12px;
        padding-bottom: 15px;
        border-bottom: 3px solid var(--accent);
      }
      .section-title i {
        color: var(--accent);
      }
      /* Form Styles */
      .exception-form {
        background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
        padding: 30px;
        border-radius: 20px;
        margin-bottom: 30px;
        border: 2px solid var(--border);
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
      }
      .form-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 25px;
        margin-bottom: 30px;
      }
      .form-group {
        display: flex;
        flex-direction: column;
      }
      .form-group label {
        font-weight: 700;
        margin-bottom: 10px;
        color: var(--primary);
        font-size: 0.95rem;
        display: flex;
        align-items: center;
        gap: 8px;
      }
      .form-group label i {
        color: var(--accent);
        font-size: 1rem;
      }
      .form-group input,
      .form-group select {
        padding: 14px 18px;
        border: 2px solid var(--border);
        border-radius: 12px;
        font-size: 0.95rem;
        transition: all 0.3s ease;
        background: #fff;
        font-family: 'Inter', sans-serif;
      }
      .form-group input:focus,
      .form-group select:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 4px rgba(236, 139, 90, 0.15);
        transform: translateY(-2px);
      }
      .form-group input::placeholder {
        color: #adb5bd;
      }
      .form-actions {
        display: flex;
        justify-content: flex-end;
        gap: 15px;
        padding-top: 20px;
        border-top: 2px solid var(--border);
      }
      .form-actions .btn {
        padding: 14px 30px;
        font-size: 1rem;
        font-weight: 700;
        box-shadow: 0 4px 15px rgba(236, 139, 90, 0.3);
      }
      .form-actions .btn:hover {
        box-shadow: 0 6px 20px rgba(236, 139, 90, 0.4);
      }
      /* Calendar View */
      .calendar-container {
        margin-top: 25px;
      }
      .calendar-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
        gap: 20px;
      }
      .calendar-list {
        display: flex;
        flex-direction: column;
        gap: 15px;
      }
      .calendar-list .calendar-day {
        display: grid;
        grid-template-columns: 140px 200px 1fr auto;
        gap: 15px;
        align-items: center;
        padding: 12px 18px;
        min-height: 70px;
      }
      .calendar-list .day-header {
        margin: 0;
        padding: 0;
        border: none;
      }
      .calendar-list .day-date {
        font-size: 0.9rem;
        font-weight: 700;
        color: var(--primary);
        display: flex;
        align-items: center;
        gap: 8px;
      }
      .calendar-list .day-date i {
        font-size: 0.85rem;
      }
      .calendar-list .day-exceptions {
        display: contents;
      }
      .calendar-list .exception-item {
        display: contents;
        background: transparent;
        padding: 0;
        border: none;
        border-radius: 0;
      }
      .calendar-list .exception-item:hover {
        transform: none;
        box-shadow: none;
      }
      .calendar-list .exception-info {
        display: flex;
        flex-direction: column;
        gap: 8px;
      }
      .calendar-list .exception-slot {
        font-weight: 600;
        color: var(--primary);
        font-size: 0.85rem;
        margin-bottom: 4px;
      }
      .calendar-list .exception-slot i {
        font-size: 0.8rem;
      }
      .calendar-list .exception-type {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        padding: 4px 10px;
        border-radius: 6px;
        font-size: 0.75rem;
        font-weight: 600;
        width: 70px;
        text-align: center;
        box-sizing: border-box;
      }
      .calendar-list .exception-reason {
        font-size: 0.8rem;
        color: var(--text-muted);
        margin-top: 3px;
      }
      .calendar-list .exception-reason i {
        font-size: 0.75rem;
      }
      .calendar-list .exception-actions {
        justify-self: end;
      }
      .view-toggle {
        display: flex;
        gap: 10px;
      }
      .view-btn {
        padding: 10px 20px;
        border: 2px solid var(--border);
        border-radius: 10px;
        background: #fff;
        color: var(--primary);
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 0.9rem;
      }
      .view-btn:hover {
        border-color: var(--accent);
        background: #fff5f0;
      }
      .view-btn.active {
        background: var(--accent);
        color: #fff;
        border-color: var(--accent);
      }
      .calendar-day {
        background: #fff;
        border: 2px solid var(--border);
        border-radius: 15px;
        padding: 20px;
        transition: all 0.3s ease;
        height: 200px;
        display: flex;
        flex-direction: column;
      }
      .calendar-day:hover {
        border-color: var(--accent);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
      }
      .day-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 2px solid var(--border);
        flex-shrink: 0;
      }
      .day-date {
        font-weight: 700;
        font-size: 0.95rem;
        color: var(--primary);
      }
      .day-exceptions {
        display: flex;
        flex-direction: column;
        gap: 10px;
        flex: 1;
      }
      .exception-item {
        background: linear-gradient(135deg, #fff5f0 0%, #ffe8d6 100%);
        padding: 12px 15px;
        border-radius: 10px;
        border-left: 4px solid var(--accent);
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        transition: all 0.3s ease;
        height: 120px;
        box-sizing: border-box;
      }
      .exception-item:hover {
        transform: translateX(5px);
        box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
      }
      .exception-info {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 8px;
      }
      .exception-slot {
        font-weight: 600;
        color: var(--primary);
        margin-bottom: 6px;
        font-size: 0.85rem;
      }
      .exception-type {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        padding: 6px 12px;
        border-radius: 8px;
        font-size: 0.8rem;
        font-weight: 600;
        margin-right: 8px;
        width: 80px;
        text-align: center;
        box-sizing: border-box;
      }
      .exception-type.OFF {
        background: var(--danger);
        color: #fff;
      }
      .exception-type.BUSY {
        background: var(--warning);
        color: #000;
      }
      .exception-type.SPECIAL {
        background: var(--info);
        color: #fff;
      }
      .exception-reason {
        font-size: 0.75rem;
        color: var(--text-muted);
        margin-top: 4px;
        line-height: 1.3;
        max-height: 32px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
      }
      .exception-actions {
        display: flex;
        gap: 8px;
      }
      .btn-icon {
        padding: 8px 10px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      .btn-icon-danger {
        background: var(--danger);
        color: #fff;
      }
      .btn-icon-danger:hover {
        background: #c82333;
        transform: scale(1.1);
      }
      .calendar-list .btn-icon {
        padding: 6px 8px;
        font-size: 0.85rem;
      }
      /* Custom Modal */
      .modal-overlay {
        display: none;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        z-index: 1000;
        align-items: center;
        justify-content: center;
        animation: fadeIn 0.3s ease;
      }
      .modal-overlay.show {
        display: flex;
      }
      @keyframes fadeIn {
        from {
          opacity: 0;
        }
        to {
          opacity: 1;
        }
      }
      .modal-dialog {
        background: #fff;
        border-radius: 20px;
        padding: 0;
        max-width: 450px;
        width: 90%;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
        animation: slideUp 0.3s ease;
        overflow: hidden;
      }
      @keyframes slideUp {
        from {
          transform: translateY(50px);
          opacity: 0;
        }
        to {
          transform: translateY(0);
          opacity: 1;
        }
      }
      .modal-header {
        background: linear-gradient(135deg, var(--danger), #c82333);
        color: #fff;
        padding: 20px 25px;
        display: flex;
        align-items: center;
        gap: 12px;
      }
      .modal-header i {
        font-size: 1.5rem;
      }
      .modal-header h3 {
        margin: 0;
        font-size: 1.2rem;
        font-weight: 700;
      }
      .modal-body {
        padding: 25px;
        color: var(--primary);
        font-size: 1rem;
        line-height: 1.6;
      }
      .modal-footer {
        padding: 20px 25px;
        display: flex;
        justify-content: flex-end;
        gap: 12px;
        border-top: 1px solid var(--border);
        background: #f8f9fa;
      }
      .modal-btn {
        padding: 10px 24px;
        border: none;
        border-radius: 10px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        font-size: 0.95rem;
      }
      .modal-btn-cancel {
        background: #6c757d;
        color: #fff;
      }
      .modal-btn-cancel:hover {
        background: #5a6268;
        transform: translateY(-2px);
      }
      .modal-btn-confirm {
        background: var(--danger);
        color: #fff;
      }
      .modal-btn-confirm:hover {
        background: #c82333;
        transform: translateY(-2px);
        box-shadow: 0 4px 15px rgba(220, 53, 69, 0.4);
      }
      /* Toast Notification */
      .toast-container {
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 2000;
        display: flex;
        flex-direction: column;
        gap: 10px;
      }
      .toast {
        background: #fff;
        border-radius: 12px;
        padding: 16px 20px;
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
        display: flex;
        align-items: center;
        gap: 12px;
        min-width: 300px;
        max-width: 400px;
        animation: slideInRight 0.4s ease, fadeOut 0.4s ease 2.6s;
        animation-fill-mode: forwards;
        border-left: 4px solid #28a745;
      }
      @keyframes slideInRight {
        from {
          transform: translateX(400px);
          opacity: 0;
        }
        to {
          transform: translateX(0);
          opacity: 1;
        }
      }
      @keyframes fadeOut {
        to {
          opacity: 0;
          transform: translateX(400px);
        }
      }
      .toast-icon {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: #28a745;
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.2rem;
        flex-shrink: 0;
      }
      .toast-content {
        flex: 1;
      }
      .toast-title {
        font-weight: 700;
        color: var(--primary);
        font-size: 1rem;
        margin-bottom: 4px;
      }
      .toast-message {
        color: var(--text-muted);
        font-size: 0.9rem;
      }
      .toast-close {
        background: none;
        border: none;
        color: var(--text-muted);
        cursor: pointer;
        font-size: 1.2rem;
        padding: 0;
        width: 24px;
        height: 24px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: color 0.3s ease;
      }
      .toast-close:hover {
        color: var(--primary);
      }
      .empty-state {
        text-align: center;
        padding: 60px 20px;
        color: var(--text-muted);
      }
      .empty-state i {
        font-size: 4rem;
        margin-bottom: 20px;
        opacity: 0.3;
      }
      .empty-state p {
        font-size: 1.1rem;
      }
      /* Week Calendar Styles */
      .week-calendar-container {
        margin-top: 20px;
        overflow-x: auto;
      }
      .week-calendar-table {
        width: 100%;
        border-collapse: collapse;
        background: #fff;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }
      .week-calendar-table thead {
        background: linear-gradient(135deg, var(--primary), #1f2961);
        color: #fff;
      }
      .week-calendar-table th {
        padding: 12px 8px;
        text-align: center;
        font-weight: 600;
        font-size: 0.9rem;
        border: 1px solid rgba(255, 255, 255, 0.2);
      }
      .week-calendar-table th.slot-header {
        background: #0d1338;
        min-width: 120px;
      }
      .week-calendar-table th small {
        display: block;
        font-size: 0.75rem;
        font-weight: 400;
        opacity: 0.9;
        margin-top: 4px;
      }
      .week-calendar-table td {
        padding: 10px 8px;
        border: 1px solid #e0e0e0;
        vertical-align: middle;
        text-align: center;
        min-width: 100px;
      }
      .slot-time-cell {
        background: #f8f9fa;
        font-weight: 600;
        color: var(--primary);
        text-align: left !important;
        padding: 12px !important;
      }
      .slot-time-cell small {
        color: var(--text-muted);
        font-weight: 400;
        font-size: 0.8rem;
      }
      .calendar-cell {
        padding: 8px !important;
        height: 80px;
      }
      .cell-status {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding: 8px 4px;
        border-radius: 8px;
        min-height: 60px;
        font-size: 0.8rem;
        font-weight: 600;
        transition: all 0.3s ease;
      }
      .cell-status i {
        font-size: 1.2rem;
        margin-bottom: 4px;
      }
      .cell-status .status-text {
        font-size: 0.75rem;
        text-align: center;
        line-height: 1.2;
      }
      .cell-exception {
        background: #ffebee;
        color: #c62828;
        border: 2px solid #ef5350;
      }
      .cell-confirmed {
        background: #e8f5e9;
        color: #2e7d32;
        border: 2px solid #4caf50;
      }
      .cell-pending {
        background: #fff3e0;
        color: #e65100;
        border: 2px solid #ff9800;
      }
      .cell-available {
        background: #e3f2fd;
        color: #1565c0;
        border: 2px solid #2196f3;
      }
      .cell-unavailable {
        background: #f5f5f5;
        color: #757575;
        border: 2px solid #bdbdbd;
      }
      .cell-empty {
        background: #fafafa;
        color: #bdbdbd;
        border: 1px dashed #e0e0e0;
      }
      .calendar-legend {
        display: flex;
        gap: 20px;
        margin-top: 20px;
        padding: 15px;
        background: #f8f9fa;
        border-radius: 8px;
        flex-wrap: wrap;
        justify-content: center;
      }
      .legend-item {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 0.85rem;
        color: var(--primary);
        font-weight: 500;
      }
      .legend-color {
        width: 20px;
        height: 20px;
        border-radius: 4px;
        border: 2px solid;
      }
      .legend-color.cell-exception {
        background: #ffebee;
        border-color: #ef5350;
      }
      .legend-color.cell-confirmed {
        background: #e8f5e9;
        border-color: #4caf50;
      }
      .legend-color.cell-pending {
        background: #fff3e0;
        border-color: #ff9800;
      }
      .legend-color.cell-available {
        background: #e3f2fd;
        border-color: #2196f3;
      }
      .legend-color.cell-unavailable {
        background: #f5f5f5;
        border-color: #bdbdbd;
      }
      .legend-color.cell-empty {
        background: #fafafa;
        border-color: #e0e0e0;
        border-style: dashed;
      }
      /* Schedule Header */
      .schedule-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 25px;
        padding-bottom: 20px;
        border-bottom: 2px solid var(--border);
      }
      .schedule-title {
        display: flex;
        align-items: center;
        gap: 15px;
      }
      .schedule-title i {
        font-size: 2rem;
        color: var(--primary);
      }
      .title-content h2 {
        margin: 0;
        font-size: 1.5rem;
        font-weight: 700;
        color: var(--primary);
        line-height: 1.2;
      }
      .title-content .week-range {
        margin: 5px 0 0 0;
        font-size: 0.95rem;
        color: var(--text-muted);
        font-weight: 500;
      }
      /* Week Navigation */
      .week-navigation {
        display: flex;
        gap: 8px;
        align-items: center;
      }
      .week-nav-btn {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        background: var(--primary);
        color: #fff;
        border-radius: 10px;
        text-decoration: none;
        font-weight: 600;
        font-size: 1rem;
        transition: all 0.3s ease;
        border: 2px solid var(--primary);
        box-shadow: 0 2px 8px rgba(20, 26, 73, 0.2);
      }
      .week-nav-btn:hover {
        background: #1f2961;
        border-color: #1f2961;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(20, 26, 73, 0.3);
      }
      .week-nav-btn i {
        font-size: 1rem;
      }
      /* Bookings Grid */
      .bookings-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
        gap: 20px;
        margin-top: 20px;
      }
      /* Bookings List View */
      .bookings-list {
        display: flex;
        flex-direction: column;
        gap: 12px;
        margin-top: 20px;
      }
      .bookings-list .booking-card {
        display: grid;
        grid-template-columns: 120px 180px 120px 180px 1fr 220px;
        gap: 15px;
        align-items: center;
        padding: 12px 18px;
        min-height: 70px;
      }
      .bookings-list .booking-header {
        display: flex;
        flex-direction: column;
        gap: 6px;
        margin: 0;
        padding: 0;
        border: none;
        justify-content: center;
      }
      .bookings-list .booking-label {
        font-size: 0.75rem;
        font-weight: 600;
        color: var(--text-muted);
        margin: 0;
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }
      .bookings-list .status-badge {
        font-size: 0.75rem;
        padding: 5px 12px;
        width: fit-content;
      }
      .bookings-list .booking-body {
        display: contents;
        margin: 0;
      }
      .bookings-list .booking-info-row {
        display: flex;
        align-items: center;
        gap: 8px;
        margin: 0;
        padding: 0;
      }
      .bookings-list .booking-info-row i {
        font-size: 0.85rem;
        color: var(--primary);
        width: 18px;
        text-align: center;
      }
      .bookings-list .info-content {
        display: flex;
        flex-direction: column;
        gap: 3px;
        flex: 1;
      }
      .bookings-list .info-label {
        font-size: 0.7rem;
        color: var(--text-muted);
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.3px;
      }
      .bookings-list .info-value {
        font-size: 0.9rem;
        color: var(--primary);
        font-weight: 500;
        line-height: 1.3;
        word-break: break-word;
      }
      .bookings-list .booking-footer {
        justify-self: end;
        margin: 0;
        padding: 0;
        display: flex;
        align-items: center;
        justify-content: flex-end;
      }
      .bookings-list .status-update-form {
        display: flex;
        gap: 8px;
        align-items: center;
        width: 100%;
        justify-content: flex-end;
      }
      .bookings-list .status-select {
        padding: 7px 10px;
        font-size: 0.85rem;
        min-width: 130px;
        border-radius: 8px;
      }
      .bookings-list .btn-sm {
        padding: 7px 14px;
        font-size: 0.85rem;
        white-space: nowrap;
      }
      .booking-card {
        background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
        border: 2px solid var(--border);
        border-radius: 15px;
        padding: 20px;
        transition: all 0.3s ease;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
      }
      .booking-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        border-color: var(--accent);
      }
      .booking-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 2px solid var(--border);
      }
      .booking-label {
        font-weight: 600;
        font-size: 0.9rem;
        color: var(--text-muted);
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }
      /* Filter Container */
      .filter-container {
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        padding: 20px;
        border-radius: 15px;
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        gap: 20px;
        flex-wrap: wrap;
      }
      .filter-label {
        font-weight: 600;
        color: var(--primary);
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 0.95rem;
      }
      .filter-label i {
        color: var(--accent);
      }
      .filter-buttons {
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
      }
      .filter-btn {
        padding: 10px 20px;
        border: 2px solid transparent;
        border-radius: 10px;
        background: #fff;
        color: var(--primary);
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 0.9rem;
      }
      .filter-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
      }
      .filter-btn.active {
        color: #fff;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
      }
      .filter-btn.active:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
      }
      .filter-btn i {
        font-size: 0.85rem;
      }
      /* All button */
      .filter-btn[data-status='all'] {
        background: var(--primary);
        border-color: var(--primary);
        color: #fff;
      }
      .filter-btn[data-status='all']:hover {
        background: #1f2961;
        border-color: #1f2961;
      }
      .filter-btn[data-status='all'].active {
        background: var(--primary);
        border-color: var(--primary);
        box-shadow: 0 0 0 3px rgba(20, 26, 73, 0.3);
      }
      /* Pending button */
      .filter-btn[data-status='pending'] {
        background: var(--warning);
        border-color: var(--warning);
        color: #000;
      }
      .filter-btn[data-status='pending']:hover {
        background: #e0a800;
        border-color: #e0a800;
      }
      .filter-btn[data-status='pending'].active {
        background: var(--warning);
        border-color: var(--warning);
        color: #000;
        box-shadow: 0 0 0 3px rgba(255, 193, 7, 0.4);
      }
      /* Confirmed button */
      .filter-btn[data-status='confirmed'] {
        background: var(--info);
        border-color: var(--info);
        color: #fff;
      }
      .filter-btn[data-status='confirmed']:hover {
        background: #138496;
        border-color: #138496;
      }
      .filter-btn[data-status='confirmed'].active {
        background: var(--info);
        border-color: var(--info);
        box-shadow: 0 0 0 3px rgba(23, 162, 184, 0.4);
      }
      /* Completed button */
      .filter-btn[data-status='completed'] {
        background: var(--success);
        border-color: var(--success);
        color: #fff;
      }
      .filter-btn[data-status='completed']:hover {
        background: #218838;
        border-color: #218838;
      }
      .filter-btn[data-status='completed'].active {
        background: var(--success);
        border-color: var(--success);
        box-shadow: 0 0 0 3px rgba(40, 167, 69, 0.4);
      }
      /* Cancelled button */
      .filter-btn[data-status='cancelled'] {
        background: var(--danger);
        border-color: var(--danger);
        color: #fff;
      }
      .filter-btn[data-status='cancelled']:hover {
        background: #c82333;
        border-color: #c82333;
      }
      .filter-btn[data-status='cancelled'].active {
        background: var(--danger);
        border-color: var(--danger);
        box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.4);
      }
      .booking-body {
        margin-bottom: 20px;
      }
      .booking-info-row {
        display: flex;
        align-items: flex-start;
        gap: 12px;
        margin-bottom: 15px;
      }
      .booking-info-row:last-child {
        margin-bottom: 0;
      }
      .booking-info-row i {
        color: var(--accent);
        font-size: 1.1rem;
        margin-top: 3px;
        min-width: 20px;
      }
      .info-content {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 4px;
      }
      .info-label {
        font-size: 0.8rem;
        color: var(--text-muted);
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }
      .info-value {
        font-size: 0.95rem;
        color: var(--primary);
        font-weight: 500;
      }
      .notes-text {
        font-style: italic;
        color: #555;
        line-height: 1.5;
      }
      .booking-footer {
        padding-top: 15px;
        border-top: 2px solid var(--border);
      }
      .status-update-form {
        display: flex;
        gap: 10px;
        align-items: center;
      }
      .status-select {
        flex: 1;
        padding: 10px 12px;
        border: 2px solid var(--border);
        border-radius: 8px;
        font-size: 0.9rem;
        background: #fff;
        transition: all 0.3s ease;
      }
      .status-select:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 3px rgba(236, 139, 90, 0.1);
      }
      /* Table Styles */
      table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
      }
      th,
      td {
        padding: 15px;
        text-align: left;
        border-bottom: 1px solid var(--border);
      }
      th {
        background: var(--primary);
        color: #fff;
        font-weight: 600;
        text-transform: uppercase;
        font-size: 0.85rem;
        letter-spacing: 0.5px;
      }
      tr:hover {
        background: #f8f9fa;
      }
      .status-badge {
        display: inline-block;
        padding: 6px 12px;
        border-radius: 12px;
        font-size: 0.8rem;
        font-weight: 600;
        color: #fff;
      }
      .status-badge.pending {
        background: var(--warning);
        color: #000;
      }
      .status-badge.confirmed {
        background: var(--info);
      }
      .status-badge.completed {
        background: var(--success);
      }
      .status-badge.cancelled {
        background: var(--danger);
      }
      .form-inline {
        display: flex;
        align-items: center;
        gap: 10px;
        flex-wrap: wrap;
      }
      .form-inline select {
        padding: 8px 12px;
        border-radius: 8px;
        border: 2px solid var(--border);
      }
      /* Week Calendar Styles */
      .week-cal {
        background: #fff;
        border-radius: 15px;
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
        overflow: hidden;
        margin-top: 20px;
      }
      .week-head,
      .week-row {
        display: grid;
        grid-template-columns: 180px repeat(7, 1fr);
      }
      .week-head div {
        background: #141a49;
        color: #fff;
        font-weight: 700;
        padding: 12px;
        text-align: center;
      }
      .slot-cell {
        border: 1px solid #eee;
        padding: 10px;
        min-height: 74px;
        display: flex;
        flex-direction: column;
        gap: 6px;
      }
      .slot-time {
        background: #f6f7fb;
        color: #141a49;
        font-weight: 700;
        padding: 10px 12px;
        border-right: 1px solid #eaeaf0;
      }
      .legend {
        display: flex;
        gap: 14px;
        align-items: center;
        margin: 14px 0 4px;
      }
      .tag {
        display: inline-flex;
        gap: 6px;
        align-items: center;
        font-size: 0.85rem;
      }
      .dot {
        width: 10px;
        height: 10px;
        border-radius: 50%;
      }
      .st-available {
        background: #28a745;
        color: #fff;
        padding: 4px 8px;
        border-radius: 8px;
        font-size: 0.78rem;
        display: inline-block;
      }
      .st-off {
        background: #f44336;
        color: #fff;
        padding: 4px 8px;
        border-radius: 8px;
        font-size: 0.78rem;
        display: inline-block;
      }
      .st-full {
        background: #ec8b5a;
        color: #fff;
        padding: 4px 8px;
        border-radius: 8px;
        font-size: 0.78rem;
        display: inline-block;
      }
      .count-chip {
        background: #141a49;
        color: #fff;
        border-radius: 999px;
        padding: 2px 8px;
        font-size: 0.75rem;
        font-weight: 700;
      }
      .small-note {
        color: #6a7890;
        font-size: 0.82rem;
      }
      @media (max-width: 768px) {
        .form-grid {
          grid-template-columns: 1fr;
        }
        .calendar-grid {
          grid-template-columns: 1fr;
        }
        .calendar-list .calendar-day {
          grid-template-columns: 1fr;
          gap: 15px;
        }
        .view-toggle {
          width: 100%;
          justify-content: center;
          margin-top: 15px;
        }
        .bookings-grid {
          grid-template-columns: 1fr;
        }
        .status-update-form {
          flex-direction: column;
        }
        .status-select {
          width: 100%;
        }
        .week-head,
        .week-row {
          grid-template-columns: 120px repeat(7, 1fr);
          font-size: 0.85rem;
        }
        .slot-time {
          padding: 8px;
          font-size: 0.8rem;
        }
        .slot-cell {
          padding: 8px;
          min-height: 60px;
        }
      }
    </style>
  </head>
  <body>
    <div class="container">
      <div class="page-header">
        <h1><i class="fas fa-calendar-alt"></i> Lịch huấn luyện của bạn</h1>
        <a href="${pageContext.request.contextPath}/pt/home" class="btn">
          <i class="fas fa-home"></i> Trang chính
        </a>
      </div>

      <!-- DANH SÁCH BUỔI TẬP -->
      <div class="card">
        <div
          style="
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
          "
        >
          <div class="section-title" style="margin: 0">
            <i class="fas fa-dumbbell"></i> Danh sách buổi tập
          </div>
          <div class="view-toggle">
            <button
              class="view-btn active"
              onclick="switchBookingView('grid')"
              id="bookingGridViewBtn"
              title="Xem dạng lưới"
            >
              <i class="fas fa-th"></i> Lưới
            </button>
            <button
              class="view-btn"
              onclick="switchBookingView('list')"
              id="bookingListViewBtn"
              title="Xem dạng danh sách"
            >
              <i class="fas fa-list"></i> Danh sách
            </button>
          </div>
        </div>
        <c:if test="${empty bookings}">
          <div class="empty-state">
            <i class="fas fa-clipboard-list"></i>
            <p>Không có buổi tập nào.</p>
          </div>
        </c:if>
        <c:if test="${not empty bookings}">
          <!-- Filter Buttons -->
          <div class="filter-container">
            <div class="filter-label">
              <i class="fas fa-filter"></i> Lọc theo trạng thái:
            </div>
            <div class="filter-buttons">
              <button
                class="filter-btn active"
                data-status="all"
                onclick="filterBookings('all')"
              >
                <i class="fas fa-list"></i> Tất cả
              </button>
              <button
                class="filter-btn"
                data-status="pending"
                onclick="filterBookings('pending')"
              >
                <i class="fas fa-clock"></i> Chờ xác nhận
              </button>
              <button
                class="filter-btn"
                data-status="confirmed"
                onclick="filterBookings('confirmed')"
              >
                <i class="fas fa-check-circle"></i> Đã xác nhận
              </button>
              <button
                class="filter-btn"
                data-status="completed"
                onclick="filterBookings('completed')"
              >
                <i class="fas fa-check-double"></i> Hoàn thành
              </button>
              <button
                class="filter-btn"
                data-status="cancelled"
                onclick="filterBookings('cancelled')"
              >
                <i class="fas fa-times-circle"></i> Đã hủy
              </button>
            </div>
          </div>
          <div class="bookings-grid" id="bookingsView">
            <c:forEach var="b" items="${bookings}">
              <div
                class="booking-card"
                data-status="${fn:toLowerCase(b.bookingStatus.name())}"
              >
                <div class="booking-header">
                  <div class="booking-label">Trạng thái:</div>
                  <span
                    class="status-badge ${fn:toLowerCase(b.bookingStatus.name())}"
                  >
                    ${b.bookingStatus.displayName}
                  </span>
                </div>
                <div class="booking-body">
                  <div class="booking-info-row">
                    <i class="fas fa-user"></i>
                    <div class="info-content">
                      <span class="info-label">Thành viên:</span>
                      <span class="info-value">
                        <c:choose>
                          <c:when test="${not empty b.member}">
                            ${b.member.name != null ? b.member.name :
                            b.member.username}
                          </c:when>
                          <c:otherwise>N/A</c:otherwise>
                        </c:choose>
                      </span>
                    </div>
                  </div>
                  <div class="booking-info-row">
                    <i class="fas fa-calendar"></i>
                    <div class="info-content">
                      <span class="info-label">Ngày:</span>
                      <span class="info-value">${b.bookingDate}</span>
                    </div>
                  </div>
                  <div class="booking-info-row">
                    <i class="fas fa-clock"></i>
                    <div class="info-content">
                      <span class="info-label">Slot:</span>
                      <span class="info-value">
                        <c:choose>
                          <c:when test="${not empty b.timeSlot}">
                            ${b.timeSlot.slotName} (${b.timeSlot.startTime} -
                            ${b.timeSlot.endTime})
                          </c:when>
                          <c:otherwise> Slot ${b.slotId} </c:otherwise>
                        </c:choose>
                      </span>
                    </div>
                  </div>
                  <c:if test="${not empty b.notes}">
                    <div class="booking-info-row">
                      <i class="fas fa-sticky-note"></i>
                      <div class="info-content">
                        <span class="info-label">Ghi chú:</span>
                        <span class="info-value notes-text">${b.notes}</span>
                      </div>
                    </div>
                  </c:if>
                </div>
                <div class="booking-footer">
                  <form
                    class="status-update-form"
                    action="${pageContext.request.contextPath}/pt/update-booking"
                    method="post"
                  >
                    <input type="hidden" name="action" value="update" />
                    <input
                      type="hidden"
                      name="bookingId"
                      value="${b.bookingId}"
                    />
                    <select name="status" class="status-select" required>
                      <option value="">-- Chọn trạng thái --</option>
                      <option value="CONFIRMED">Xác nhận</option>
                      <option value="COMPLETED">Hoàn thành</option>
                      <option value="CANCELLED">Hủy</option>
                    </select>
                    <button type="submit" class="btn btn-sm">
                      <i class="fas fa-sync"></i> Cập nhật
                    </button>
                  </form>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:if>
      </div>

      <!-- ĐĂNG KÝ NGÀY NGHỈ / BẬN -->
      <div class="card">
        <div class="section-title">
          <i class="fas fa-bed"></i> Đăng ký ngày nghỉ/bận
        </div>
        <div class="exception-form">
          <form
            action="${pageContext.request.contextPath}/pt/add-exception"
            method="post"
          >
            <input type="hidden" name="action" value="exception" />
            <input
              type="hidden"
              name="trainerId"
              value="${sessionScope.user.id}"
            />
            <div class="form-grid">
              <div class="form-group">
                <label><i class="fas fa-calendar"></i> Ngày</label>
                <input type="date" name="date" id="exceptionDate" required />
              </div>
              <div class="form-group">
                <label><i class="fas fa-clock"></i> Slot</label>
                <select name="slotId" required>
                  <option value="">-- Chọn slot --</option>
                  <c:forEach var="slot" items="${timeSlots}">
                    <option value="${slot.slotId}">
                      ${slot.slotName} (${slot.startTime} - ${slot.endTime})
                    </option>
                  </c:forEach>
                </select>
              </div>
              <div class="form-group">
                <label><i class="fas fa-tag"></i> Loại</label>
                <select name="type" required>
                  <option value="OFF">Nghỉ (OFF)</option>
                  <option value="BUSY">Bận (BUSY)</option>
                  <option value="SPECIAL">Đặc biệt (SPECIAL)</option>
                </select>
              </div>
              <div class="form-group">
                <label><i class="fas fa-comment"></i> Lý do</label>
                <input
                  type="text"
                  name="reason"
                  placeholder="Nhập lý do (tùy chọn)"
                />
              </div>
            </div>
            <div class="form-actions">
              <button type="submit" class="btn">
                <i class="fas fa-calendar-plus"></i> Đăng ký ngày nghỉ
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- LỊCH NGHỈ / BẬN ĐÃ ĐĂNG KÝ -->
      <div class="card" id="exceptionsSection">
        <div
          style="
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
          "
        >
          <div class="section-title" style="margin: 0">
            <i class="fas fa-calendar-times"></i> Ngày nghỉ/bận đã đăng ký
          </div>
          <div class="view-toggle">
            <button
              class="view-btn active"
              onclick="switchView('grid')"
              id="gridViewBtn"
              title="Xem dạng lưới"
            >
              <i class="fas fa-th"></i> Lưới
            </button>
            <button
              class="view-btn"
              onclick="switchView('list')"
              id="listViewBtn"
              title="Xem dạng danh sách"
            >
              <i class="fas fa-list"></i> Danh sách
            </button>
          </div>
        </div>
        <c:if test="${empty exceptions}">
          <div class="empty-state">
            <i class="fas fa-calendar-check"></i>
            <p>Chưa có ngày nghỉ/bận nào được đăng ký.</p>
          </div>
        </c:if>
        <c:if test="${not empty exceptions}">
          <div class="calendar-container">
            <div class="calendar-grid" id="calendarView">
              <c:forEach var="exception" items="${exceptions}">
                <div class="calendar-day">
                  <div class="day-header">
                    <div class="day-date">
                      <i class="fas fa-calendar-day"></i>
                      ${exception.exceptionDate}
                    </div>
                  </div>
                  <div class="day-exceptions">
                    <div class="exception-item">
                      <div class="exception-info">
                        <div class="exception-slot">
                          <i class="fas fa-clock"></i>
                          <c:forEach var="slot" items="${timeSlots}">
                            <c:if test="${slot.slotId == exception.slotId}">
                              ${slot.slotName} (${slot.startTime} -
                              ${slot.endTime})
                            </c:if>
                          </c:forEach>
                          <c:if test="${empty exception.slotId}">
                            Tất cả các slot
                          </c:if>
                        </div>
                        <div>
                          <span
                            class="exception-type ${exception.exceptionType}"
                          >
                            ${exception.exceptionType}
                          </span>
                        </div>
                        <c:if test="${not empty exception.reason}">
                          <div class="exception-reason">
                            <i class="fas fa-comment"></i> ${exception.reason}
                          </div>
                        </c:if>
                      </div>
                      <div class="exception-actions">
                        <form
                          action="${pageContext.request.contextPath}/pt/delete-exception"
                          method="post"
                          style="display: inline"
                        >
                          <input
                            type="hidden"
                            name="action"
                            value="delete-exception"
                          />
                          <input
                            type="hidden"
                            name="exceptionId"
                            value="${exception.exceptionId}"
                          />
                          <button
                            type="button"
                            class="btn-icon btn-icon-danger"
                            onclick="showDeleteModal('${exception.exceptionId}')"
                          >
                            <i class="fas fa-trash"></i>
                          </button>
                        </form>
                      </div>
                    </div>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </c:if>
      </div>

      <!-- LỊCH RIÊNG CỦA PT (Tuần hiện tại) -->
      <div class="card" id="weeklyScheduleSection">
        <div class="schedule-header">
          <div class="schedule-title">
            <i class="fas fa-calendar-week"></i>
            <div class="title-content">
              <h2>Lịch riêng của PT</h2>
              <p class="week-range">Tuần ${weekStart} đến ${weekEnd}</p>
            </div>
          </div>
          <div class="week-navigation">
            <a
              href="?week=${weekStart.minusWeeks(1)}#weeklyScheduleSection"
              class="week-nav-btn"
              title="Tuần trước"
            >
              <i class="fas fa-chevron-left"></i>
            </a>
            <a
              href="?week=${weekStart.plusWeeks(1)}#weeklyScheduleSection"
              class="week-nav-btn"
              title="Tuần sau"
            >
              <i class="fas fa-chevron-right"></i>
            </a>
          </div>
        </div>
        <c:if test="${empty fixedTimeSlots || empty fixedMap}">
          <div class="empty-state">
            <i class="fas fa-calendar-alt"></i>
            <p>Chưa có lịch làm việc cố định.</p>
          </div>
        </c:if>
        <c:if test="${not empty fixedTimeSlots && not empty fixedMap}">
          <div class="week-calendar-container">
            <table class="week-calendar-table">
              <thead>
                <tr>
                  <th class="slot-header">Khung giờ</th>
                  <th>Thứ 2<br /><small>${weekStart}</small></th>
                  <th>Thứ 3<br /><small>${weekStart.plusDays(1)}</small></th>
                  <th>Thứ 4<br /><small>${weekStart.plusDays(2)}</small></th>
                  <th>Thứ 5<br /><small>${weekStart.plusDays(3)}</small></th>
                  <th>Thứ 6<br /><small>${weekStart.plusDays(4)}</small></th>
                  <th>Thứ 7<br /><small>${weekStart.plusDays(5)}</small></th>
                  <th>CN<br /><small>${weekStart.plusDays(6)}</small></th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="slot" items="${fixedTimeSlots}">
                  <tr>
                    <td class="slot-time-cell">
                      <strong>${slot[1]}</strong> - ${slot[2]}<br />
                      <small>Slot ${slot[0]}</small>
                    </td>
                    <c:forEach var="dayOffset" begin="0" end="6">
                      <c:set
                        var="currentDate"
                        value="${weekStart.plusDays(dayOffset)}"
                      />
                      <c:set var="cellKey" value="${currentDate}#${slot[0]}" />
                      <c:set var="cellData" value="${fixedMap[cellKey]}" />
                      <td class="calendar-cell">
                        <c:choose>
                          <c:when test="${not empty cellData}">
                            <%-- row: [0]=slotId, [1]=actualDate, [2]=dayOfWeek,
                            [3]=confirmedCount, [4]=pendingCount,
                            [5]=hasException, [6]=exceptionType,
                            [7]=hasSchedule, [8]=isAvailable --%>
                            <c:set
                              var="confirmedCount"
                              value="${cellData[3]}"
                            />
                            <c:set var="pendingCount" value="${cellData[4]}" />
                            <c:set var="hasException" value="${cellData[5]}" />
                            <c:set var="exceptionType" value="${cellData[6]}" />
                            <c:set var="hasSchedule" value="${cellData[7]}" />
                            <c:set var="isAvailable" value="${cellData[8]}" />

                            <c:choose>
                              <%-- Ưu tiên 1: Có exception (Nghỉ/Bận) --%>
                              <c:when test="${hasException == 1}">
                                <div class="cell-status cell-exception">
                                  <i class="fas fa-ban"></i>
                                  <span class="status-text">
                                    <c:choose>
                                      <c:when test="${exceptionType == 'OFF'}"
                                        >Nghỉ</c:when
                                      >
                                      <c:when test="${exceptionType == 'BUSY'}"
                                        >Bận</c:when
                                      >
                                      <c:otherwise>Đặc biệt</c:otherwise>
                                    </c:choose>
                                  </span>
                                </div>
                              </c:when>
                              <%-- Ưu tiên 2: Có buổi Confirmed --%>
                              <c:when test="${confirmedCount > 0}">
                                <div class="cell-status cell-confirmed">
                                  <i class="fas fa-check-circle"></i>
                                  <span class="status-text"
                                    >Đã xác nhận (${confirmedCount})</span
                                  >
                                </div>
                              </c:when>
                              <%-- Ưu tiên 3: Có buổi Pending --%>
                              <c:when test="${pendingCount > 0}">
                                <div class="cell-status cell-pending">
                                  <i class="fas fa-clock"></i>
                                  <span class="status-text"
                                    >Chờ xác nhận (${pendingCount})</span
                                  >
                                </div>
                              </c:when>
                              <%-- Ưu tiên 4: Có schedule nhưng không available
                              --%>
                              <c:when
                                test="${hasSchedule == 1 && isAvailable == 0}"
                              >
                                <div class="cell-status cell-unavailable">
                                  <i class="fas fa-times"></i>
                                  <span class="status-text">Không có ca</span>
                                </div>
                              </c:when>
                              <%-- Mặc định: Còn trống (có schedule và
                              available) --%>
                              <c:when
                                test="${hasSchedule == 1 && isAvailable == 1}"
                              >
                                <div class="cell-status cell-available">
                                  <i class="fas fa-check"></i>
                                  <span class="status-text">Còn trống</span>
                                </div>
                              </c:when>
                              <%-- Không có schedule --%>
                              <c:otherwise>
                                <div class="cell-status cell-empty">
                                  <span class="status-text">-</span>
                                </div>
                              </c:otherwise>
                            </c:choose>
                          </c:when>
                          <c:otherwise>
                            <div class="cell-status cell-empty">
                              <span class="status-text">-</span>
                            </div>
                          </c:otherwise>
                        </c:choose>
                      </td>
                    </c:forEach>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
            <div class="calendar-legend">
              <span class="legend-item"
                ><span class="legend-color cell-exception"></span>
                Nghỉ/Bận</span
              >
              <span class="legend-item"
                ><span class="legend-color cell-confirmed"></span> Đã xác
                nhận</span
              >
              <span class="legend-item"
                ><span class="legend-color cell-pending"></span> Chờ xác
                nhận</span
              >
              <span class="legend-item"
                ><span class="legend-color cell-available"></span> Còn trống (Có
                ca làm, chưa có booking)</span
              >
              <span class="legend-item"
                ><span class="legend-color cell-unavailable"></span> Không có ca
                (PT không làm việc slot này)</span
              >
              <span class="legend-item"
                ><span class="legend-color cell-empty"></span> - (Chưa thiết lập
                lịch cố định)</span
              >
            </div>
          </div>
        </c:if>
      </div>
    </div>

    <!-- Toast Notification Container -->
    <div class="toast-container" id="toastContainer"></div>

    <!-- Custom Delete Modal -->
    <div class="modal-overlay" id="deleteModal">
      <div class="modal-dialog">
        <div class="modal-header">
          <i class="fas fa-exclamation-triangle"></i>
          <h3>Xác nhận xóa</h3>
        </div>
        <div class="modal-body">
          <p>
            Bạn có chắc chắn muốn xóa ngày nghỉ/bận này? Hành động này không thể
            hoàn tác.
          </p>
        </div>
        <div class="modal-footer">
          <button
            class="modal-btn modal-btn-cancel"
            onclick="closeDeleteModal()"
          >
            <i class="fas fa-times"></i> Hủy
          </button>
          <form id="deleteForm" method="post" style="display: inline">
            <input type="hidden" name="action" value="delete-exception" />
            <input type="hidden" name="exceptionId" id="deleteExceptionId" />
            <button type="submit" class="modal-btn modal-btn-confirm">
              <i class="fas fa-trash"></i> Xóa
            </button>
          </form>
        </div>
      </div>
    </div>

    <script>
      // Set min date to today and scroll to weekly schedule if needed
      document.addEventListener('DOMContentLoaded', function () {
        const dateInput = document.getElementById('exceptionDate');
        if (dateInput) {
          const today = new Date().toISOString().split('T')[0];
          dateInput.setAttribute('min', today);
        }

        // Scroll to weekly schedule section if URL has anchor
        if (window.location.hash === '#weeklyScheduleSection') {
          setTimeout(function () {
            const section = document.getElementById('weeklyScheduleSection');
            if (section) {
              section.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
          }, 100);
        }

        // Check for success messages (add or delete)
        var addSuccessMessage = '<c:out value="${sessionScope.addSuccess}" />';
        var deleteSuccessMessage =
          '<c:out value="${sessionScope.deleteSuccess}" />';

        console.log('Add success message:', addSuccessMessage);
        console.log('Delete success message:', deleteSuccessMessage);

        // Handle add success message
        if (
          addSuccessMessage &&
          addSuccessMessage.trim() !== '' &&
          addSuccessMessage !== 'null'
        ) {
          console.log('Showing toast and scrolling for add success...');
          showToast(addSuccessMessage);
          // Scroll to exceptions section after a short delay
          setTimeout(function () {
            const exceptionsSection =
              document.getElementById('exceptionsSection');
            console.log('Exceptions section:', exceptionsSection);
            if (exceptionsSection) {
              exceptionsSection.scrollIntoView({
                behavior: 'smooth',
                block: 'start',
              });
            }
          }, 500);

          // Remove message from session after displaying
          fetch('${pageContext.request.contextPath}/pt/clear-message', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'action=clear-add-success',
          }).catch(function (err) {
            console.log('Error clearing message:', err);
          });
        }

        // Handle delete success message
        if (
          deleteSuccessMessage &&
          deleteSuccessMessage.trim() !== '' &&
          deleteSuccessMessage !== 'null'
        ) {
          console.log('Showing toast and scrolling for delete success...');
          showToast(deleteSuccessMessage);
          // Scroll to exceptions section after a short delay
          setTimeout(function () {
            const exceptionsSection =
              document.getElementById('exceptionsSection');
            console.log('Exceptions section:', exceptionsSection);
            if (exceptionsSection) {
              exceptionsSection.scrollIntoView({
                behavior: 'smooth',
                block: 'start',
              });
            }
          }, 500);

          // Remove message from session after displaying
          fetch('${pageContext.request.contextPath}/pt/clear-message', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'action=clear-delete-success',
          }).catch(function (err) {
            console.log('Error clearing message:', err);
          });
        }
      });

      // Toast notification function
      function showToast(message) {
        console.log('showToast called with message:', message);
        const toastContainer = document.getElementById('toastContainer');
        if (!toastContainer) {
          console.error('Toast container not found!');
          return;
        }
        const toast = document.createElement('div');
        toast.className = 'toast';
        toast.innerHTML = `
          <div class="toast-icon">
            <i class="fas fa-check"></i>
          </div>
          <div class="toast-content">
            <div class="toast-title">Thành công!</div>
            <div class="toast-message">${message}</div>
          </div>
          <button class="toast-close" onclick="this.parentElement.remove()">
            <i class="fas fa-times"></i>
          </button>
        `;
        toastContainer.appendChild(toast);

        // Auto remove after 3 seconds
        setTimeout(function () {
          if (toast.parentElement) {
            toast.style.animation = 'fadeOut 0.4s ease';
            setTimeout(function () {
              toast.remove();
            }, 400);
          }
        }, 3000);
      }

      // Switch between grid and list view for exceptions
      function switchView(viewType) {
        const calendarView = document.getElementById('calendarView');
        const gridBtn = document.getElementById('gridViewBtn');
        const listBtn = document.getElementById('listViewBtn');

        if (viewType === 'grid') {
          calendarView.className = 'calendar-grid';
          gridBtn.classList.add('active');
          listBtn.classList.remove('active');
        } else {
          calendarView.className = 'calendar-list';
          listBtn.classList.add('active');
          gridBtn.classList.remove('active');
        }
      }

      // Switch between grid and list view for bookings
      function switchBookingView(viewType) {
        const bookingsView = document.getElementById('bookingsView');
        const gridBtn = document.getElementById('bookingGridViewBtn');
        const listBtn = document.getElementById('bookingListViewBtn');

        if (viewType === 'grid') {
          bookingsView.className = 'bookings-grid';
          gridBtn.classList.add('active');
          listBtn.classList.remove('active');
        } else {
          bookingsView.className = 'bookings-list';
          listBtn.classList.add('active');
          gridBtn.classList.remove('active');
        }
      }

      // Delete Modal Functions
      function showDeleteModal(exceptionId) {
        document.getElementById('deleteExceptionId').value = exceptionId;
        document.getElementById('deleteForm').action =
          '${pageContext.request.contextPath}/pt/delete-exception';
        document.getElementById('deleteModal').classList.add('show');
      }

      function closeDeleteModal() {
        document.getElementById('deleteModal').classList.remove('show');
      }

      // Close modal when clicking outside
      document
        .getElementById('deleteModal')
        .addEventListener('click', function (e) {
          if (e.target === this) {
            closeDeleteModal();
          }
        });

      // Close modal with Escape key
      document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
          closeDeleteModal();
        }
      });

      function filterBookings(status) {
        // Update active button
        document.querySelectorAll('.filter-btn').forEach((btn) => {
          btn.classList.remove('active');
          if (btn.getAttribute('data-status') === status) {
            btn.classList.add('active');
          }
        });

        // Filter cards
        const cards = document.querySelectorAll('.booking-card');
        let visibleCount = 0;
        cards.forEach((card) => {
          if (status === 'all') {
            card.style.display = 'block';
            visibleCount++;
          } else {
            const cardStatus = card.getAttribute('data-status');
            if (cardStatus === status) {
              card.style.display = 'block';
              visibleCount++;
            } else {
              card.style.display = 'none';
            }
          }
        });

        // Show message if no results
        let messageEl = document.getElementById('noResultsMessage');
        if (visibleCount === 0 && status !== 'all') {
          if (!messageEl) {
            messageEl = document.createElement('div');
            messageEl.id = 'noResultsMessage';
            messageEl.className = 'empty-state';
            messageEl.innerHTML =
              '<i class="fas fa-inbox"></i><p>Không có buổi tập nào với trạng thái này.</p>';
            document.getElementById('bookingsGrid').appendChild(messageEl);
          }
          messageEl.style.display = 'block';
        } else {
          if (messageEl) {
            messageEl.style.display = 'none';
          }
        }
      }
    </script>
  </body>
</html>
