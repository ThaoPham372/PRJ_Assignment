/**
 * Main JavaScript file for Gym Management System
 * Contains common utilities and functions used across the application
 */

// Global configuration
const GymManager = {
  baseUrl: window.location.origin + '/gym-management',

  // Initialize common functionality
  init: function () {
    this.setupDataTables();
    this.setupFormValidation();
    this.setupDeleteConfirmation();
    this.setupTooltips();
    this.setupDatePickers();
    this.setupNumberFormatting();
  },

  // Setup DataTables for list pages
  setupDataTables: function () {
    if (typeof $ !== 'undefined' && $.fn.DataTable) {
      $('.data-table').DataTable({
        language: {
          url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/vi.json',
        },
        pageLength: 10,
        responsive: true,
        order: [[0, 'desc']],
        columnDefs: [{ orderable: false, targets: 'no-sort' }],
      });
    }
  },

  // Setup form validation
  setupFormValidation: function () {
    // Customer code validation: KH-XXXX
    const customerCodeInputs = document.querySelectorAll(
      'input[name="customerCode"]',
    );
    customerCodeInputs.forEach((input) => {
      input.addEventListener('blur', function () {
        const pattern = /^KH-\d{4}$/;
        if (this.value && !pattern.test(this.value)) {
          this.setCustomValidity(
            'Mã khách hàng phải có định dạng KH-XXXX (X là số 0-9)',
          );
        } else {
          this.setCustomValidity('');
        }
      });
    });

    // Service code validation: DV-XXXX
    const serviceCodeInputs = document.querySelectorAll(
      'input[name="serviceCode"]',
    );
    serviceCodeInputs.forEach((input) => {
      input.addEventListener('blur', function () {
        const pattern = /^DV-\d{4}$/;
        if (this.value && !pattern.test(this.value)) {
          this.setCustomValidity(
            'Mã dịch vụ phải có định dạng DV-XXXX (X là số 0-9)',
          );
        } else {
          this.setCustomValidity('');
        }
      });
    });

    // Phone number validation
    const phoneInputs = document.querySelectorAll(
      'input[name="phone"], input[type="tel"]',
    );
    phoneInputs.forEach((input) => {
      input.addEventListener('blur', function () {
        const patterns = [
          /^090\d{7}$/, // 090xxxxxxx
          /^091\d{8}$/, // 091xxxxxxxx
          /^\(84\)\+90\d{7}$/, // (84)+90xxxxxxxx
          /^\(84\)\+91\d{8}$/, // (84)+91xxxxxxxx
        ];

        if (
          this.value &&
          !patterns.some((pattern) => pattern.test(this.value))
        ) {
          this.setCustomValidity(
            'Số điện thoại phải có định dạng: 090xxxxxxx, 091xxxxxxxx, (84)+90xxxxxxxx, hoặc (84)+91xxxxxxxx',
          );
        } else {
          this.setCustomValidity('');
        }
      });
    });

    // ID number validation
    const idInputs = document.querySelectorAll('input[name="idNumber"]');
    idInputs.forEach((input) => {
      input.addEventListener('blur', function () {
        const patterns = [
          /^\d{9}$/, // 9 digits
          /^\d{13}$/, // 13 digits
        ];

        if (
          this.value &&
          !patterns.some((pattern) => pattern.test(this.value))
        ) {
          this.setCustomValidity('Số CMND/CCCD phải có 9 hoặc 13 chữ số');
        } else {
          this.setCustomValidity('');
        }
      });
    });

    // Positive number validation
    const positiveInputs = document.querySelectorAll(
      'input[data-positive="true"]',
    );
    positiveInputs.forEach((input) => {
      input.addEventListener('blur', function () {
        const value = parseFloat(this.value);
        if (this.value && (isNaN(value) || value <= 0)) {
          this.setCustomValidity('Giá trị phải là số dương');
        } else {
          this.setCustomValidity('');
        }
      });
    });
  },

  // Setup delete confirmation
  setupDeleteConfirmation: function () {
    const deleteButtons = document.querySelectorAll(
      '.btn-delete, [data-action="delete"]',
    );
    deleteButtons.forEach((button) => {
      button.addEventListener('click', function (e) {
        e.preventDefault();

        const itemName = this.getAttribute('data-item') || 'mục này';
        const confirmMsg = `Bạn có chắc chắn muốn xóa ${itemName}?\n\nThao tác này không thể hoàn tác.`;

        if (confirm(confirmMsg)) {
          // If it's a form, submit it
          const form = this.closest('form');
          if (form) {
            form.submit();
          }
          // If it's a link, follow it
          else if (this.href) {
            window.location.href = this.href;
          }
          // If it has data-url, redirect there
          else if (this.getAttribute('data-url')) {
            window.location.href = this.getAttribute('data-url');
          }
        }
      });
    });
  },

  // Setup tooltips
  setupTooltips: function () {
    if (typeof bootstrap !== 'undefined') {
      const tooltipTriggerList = [].slice.call(
        document.querySelectorAll('[data-bs-toggle="tooltip"]'),
      );
      tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
      });
    }
  },

  // Setup date pickers
  setupDatePickers: function () {
    const dateInputs = document.querySelectorAll('input[type="date"]');
    dateInputs.forEach((input) => {
      // Set max date to today for birth dates
      if (input.name === 'dateOfBirth' || input.name === 'dob') {
        input.max = new Date().toISOString().split('T')[0];
      }

      // Validate date format
      input.addEventListener('blur', function () {
        if (this.value) {
          const date = new Date(this.value);
          if (isNaN(date.getTime())) {
            this.setCustomValidity('Ngày tháng không hợp lệ');
          } else {
            this.setCustomValidity('');
          }
        }
      });
    });
  },

  // Setup number formatting
  setupNumberFormatting: function () {
    const currencyInputs = document.querySelectorAll(
      'input[data-currency="true"]',
    );
    currencyInputs.forEach((input) => {
      input.addEventListener('blur', function () {
        if (this.value) {
          const value = parseFloat(this.value.replace(/[^\d]/g, ''));
          if (!isNaN(value)) {
            this.value = value.toLocaleString('vi-VN');
          }
        }
      });

      input.addEventListener('focus', function () {
        this.value = this.value.replace(/[^\d]/g, '');
      });
    });
  },

  // Utility functions
  utils: {
    // Show toast notification
    showToast: function (message, type = 'success') {
      const toastContainer =
        document.getElementById('toast-container') ||
        this.createToastContainer();

      const toast = document.createElement('div');
      toast.className = `toast align-items-center text-white bg-${type} border-0`;
      toast.setAttribute('role', 'alert');
      toast.innerHTML = `
                <div class="d-flex">
                    <div class="toast-body">
                        <i class="fas ${
                          type === 'success'
                            ? 'fa-check-circle'
                            : 'fa-exclamation-circle'
                        } me-2"></i>
                        ${message}
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                </div>
            `;

      toastContainer.appendChild(toast);

      if (typeof bootstrap !== 'undefined') {
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();

        toast.addEventListener('hidden.bs.toast', () => {
          toast.remove();
        });
      } else {
        setTimeout(() => toast.remove(), 5000);
      }
    },

    // Create toast container if not exists
    createToastContainer: function () {
      const container = document.createElement('div');
      container.id = 'toast-container';
      container.className = 'toast-container position-fixed top-0 end-0 p-3';
      container.style.zIndex = '1055';
      document.body.appendChild(container);
      return container;
    },

    // Format currency
    formatCurrency: function (amount) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
      }).format(amount);
    },

    // Format date
    formatDate: function (date) {
      return new Intl.DateTimeFormat('vi-VN').format(new Date(date));
    },

    // AJAX helper
    ajax: function (url, options = {}) {
      const defaultOptions = {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'X-Requested-With': 'XMLHttpRequest',
        },
      };

      const finalOptions = Object.assign(defaultOptions, options);

      return fetch(url, finalOptions)
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }
          return response.json();
        })
        .catch((error) => {
          console.error('AJAX Error:', error);
          this.showToast('Có lỗi xảy ra khi xử lý yêu cầu', 'danger');
          throw error;
        });
    },
  },
};

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function () {
  GymManager.init();
});

// Export for global use
window.GymManager = GymManager;
