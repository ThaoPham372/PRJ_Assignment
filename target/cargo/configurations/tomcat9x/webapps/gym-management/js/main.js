/**
 * Stamina Gym Management System - Main JavaScript
 * Contains common utilities and functions used across the application
 * Author: Stamina Gym Team
 * Version: 1.0.0
 */

// Global configuration
const StaminaGym = {
  baseUrl: window.location.origin + '/gym-management',
  theme: {
    colors: {
      primary: '#3B1E78',
      secondary: '#FFD700',
      success: '#28A745',
      danger: '#DC3545',
      warning: '#FFC107',
      info: '#17A2B8',
    },
  },

  // Initialize common functionality
  init: function () {
    this.setupDataTables();
    this.setupFormValidation();
    this.setupDeleteConfirmation();
    this.setupTooltips();
    this.setupDatePickers();
    this.setupNumberFormatting();
    this.setupScrollAnimations();
    this.setupNavbarHighlight();
    this.setupResponsiveHandling();
    console.log('🏋️ Stamina Gym System Initialized');
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

  // Setup scroll animations
  setupScrollAnimations: function () {
    const animateElements = document.querySelectorAll(
      '.stat-card, .coach-card, .package-card',
    );

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.style.animation = 'fadeInUp 0.6s ease forwards';
          }
        });
      },
      { threshold: 0.1 },
    );

    animateElements.forEach((el) => {
      observer.observe(el);
    });

    // Add CSS animation
    if (!document.getElementById('scroll-animations')) {
      const style = document.createElement('style');
      style.id = 'scroll-animations';
      style.textContent = `
        @keyframes fadeInUp {
          from {
            opacity: 0;
            transform: translateY(30px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
      `;
      document.head.appendChild(style);
    }
  },

  // Setup navbar highlight on scroll
  setupNavbarHighlight: function () {
    const navbar = document.querySelector('.navbar');
    if (navbar) {
      window.addEventListener('scroll', () => {
        if (window.scrollY > 100) {
          navbar.classList.add('scrolled');
        } else {
          navbar.classList.remove('scrolled');
        }
      });
    }

    // Smooth scroll for anchor links
    document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
      anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
          target.scrollIntoView({
            behavior: 'smooth',
            block: 'start',
          });
        }
      });
    });
  },

  // Setup responsive handling
  setupResponsiveHandling: function () {
    // Mobile sidebar toggle
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');

    if (sidebarToggle && sidebar) {
      sidebarToggle.addEventListener('click', () => {
        sidebar.classList.toggle('show');
      });

      // Close sidebar when clicking outside on mobile
      document.addEventListener('click', (e) => {
        if (
          window.innerWidth <= 992 &&
          !sidebar.contains(e.target) &&
          !sidebarToggle.contains(e.target)
        ) {
          sidebar.classList.remove('show');
        }
      });
    }

    // Handle window resize
    window.addEventListener('resize', () => {
      if (window.innerWidth > 992 && sidebar) {
        sidebar.classList.remove('show');
      }
    });
  },

  // Enhanced validation with Stamina Gym specific rules
  validateMembershipId: function (id) {
    const pattern = /^GM\d{4}$/;
    return pattern.test(id);
  },

  validateCoachId: function (id) {
    const pattern = /^PT\d{3}$/;
    return pattern.test(id);
  },

  validateEquipmentId: function (id) {
    const pattern = /^EQ\d{3}$/;
    return pattern.test(id);
  },
};

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function () {
  StaminaGym.init();
});

// Export for global use
window.StaminaGym = StaminaGym;
window.GymManager = StaminaGym; // Backward compatibility
