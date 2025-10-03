/**
 * Stamina Gym Management System - Main JavaScript
 * Contains client-side UI enhancements and utilities
 * Server-side functionality is handled by servlets
 * Author: Stamina Gym Team
 * Version: 2.0.0
 */

// Global configuration
const StaminaGym = {
  theme: {
    colors: {
      primary: '#6B46C1',
      secondary: '#FCD34D',
      success: '#10B981',
      danger: '#EF4444',
      warning: '#F59E0B',
      info: '#3B82F6',
    },
  },

  // Initialize client-side functionality
  init: function () {
    this.setupScrollAnimations();
    this.setupNavbarHighlight();
    this.setupResponsiveHandling();
    this.setupFormEnhancements();
    console.log('🏋️ Stamina Gym Client Initialized');
  },

  // Setup scroll animations for home page
  setupScrollAnimations: function () {
    const animateElements = document.querySelectorAll(
      '.stat-item, .coach-card, .pricing-card',
    );

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.style.animation = 'fadeInUp 0.8s ease forwards';
          }
        });
      },
      { threshold: 0.1 },
    );

    animateElements.forEach((el) => {
      observer.observe(el);
    });

    // Add CSS animation if not exists
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
        .stat-item, .coach-card, .pricing-card {
          opacity: 0;
        }
      `;
      document.head.appendChild(style);
    }
  },

  // Setup navbar highlight and smooth scrolling
  setupNavbarHighlight: function () {
    const navbar = document.querySelector('.navbar');
    if (navbar) {
      window.addEventListener('scroll', () => {
        if (window.scrollY > 100) {
          navbar.style.background = 'rgba(255, 255, 255, 0.98)';
          navbar.style.boxShadow = '0 2px 20px rgba(0,0,0,0.15)';
        } else {
          navbar.style.background = 'rgba(255, 255, 255, 0.95)';
          navbar.style.boxShadow = '0 2px 20px rgba(0,0,0,0.1)';
        }
      });
    }

    // Smooth scroll for anchor links
    document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
      anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
          const offsetTop = target.offsetTop - 80; // Account for fixed navbar
          window.scrollTo({
            top: offsetTop,
            behavior: 'smooth',
          });
        }
      });
    });
  },

  // Setup responsive handling
  setupResponsiveHandling: function () {
    // Mobile navbar toggle
    const navbarToggler = document.querySelector('.navbar-toggler');
    const navbarCollapse = document.querySelector('.navbar-collapse');

    if (navbarToggler && navbarCollapse) {
      // Close navbar when clicking outside on mobile
      document.addEventListener('click', (e) => {
        if (
          window.innerWidth <= 992 &&
          !navbarCollapse.contains(e.target) &&
          !navbarToggler.contains(e.target) &&
          navbarCollapse.classList.contains('show')
        ) {
          navbarToggler.click();
        }
      });
    }

    // Handle window resize
    window.addEventListener('resize', () => {
      if (
        window.innerWidth > 992 &&
        navbarCollapse &&
        navbarCollapse.classList.contains('show')
      ) {
        navbarToggler.click();
      }
    });
  },

  // Setup form enhancements (client-side only)
  setupFormEnhancements: function () {
    // Add loading state to form submissions
    const forms = document.querySelectorAll('form');
    forms.forEach((form) => {
      form.addEventListener('submit', function (e) {
        const submitBtn = this.querySelector('button[type="submit"]');
        if (submitBtn) {
          submitBtn.disabled = true;
          submitBtn.innerHTML =
            '<i class="fas fa-spinner fa-spin me-2"></i>Đang xử lý...';

          // Re-enable after 5 seconds as fallback
          setTimeout(() => {
            submitBtn.disabled = false;
            submitBtn.innerHTML =
              submitBtn.getAttribute('data-original-text') || 'Gửi';
          }, 5000);
        }
      });
    });

    // Store original button text
    document.querySelectorAll('button[type="submit"]').forEach((btn) => {
      btn.setAttribute('data-original-text', btn.innerHTML);
    });

    // Basic client-side validation feedback
    const inputs = document.querySelectorAll(
      'input[required], select[required], textarea[required]',
    );
    inputs.forEach((input) => {
      input.addEventListener('blur', function () {
        if (this.value.trim() === '') {
          this.classList.add('is-invalid');
        } else {
          this.classList.remove('is-invalid');
          this.classList.add('is-valid');
        }
      });

      input.addEventListener('input', function () {
        if (this.classList.contains('is-invalid') && this.value.trim() !== '') {
          this.classList.remove('is-invalid');
          this.classList.add('is-valid');
        }
      });
    });
  },

  // Utility functions for client-side operations
  utils: {
    // Show toast notification (client-side only)
    showToast: function (message, type = 'success') {
      // Create toast container if not exists
      let toastContainer = document.getElementById('toast-container');
      if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toast-container';
        toastContainer.className =
          'toast-container position-fixed top-0 end-0 p-3';
        toastContainer.style.zIndex = '1055';
        document.body.appendChild(toastContainer);
      }

      const toast = document.createElement('div');
      toast.className = `toast align-items-center text-white bg-${type} border-0`;
      toast.setAttribute('role', 'alert');
      toast.innerHTML = `
        <div class="d-flex">
          <div class="toast-body">
            <i class="fas ${
              type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'
            } me-2"></i>
            ${message}
          </div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" onclick="this.parentElement.parentElement.remove()"></button>
        </div>
      `;

      toastContainer.appendChild(toast);

      // Auto remove after 5 seconds
      setTimeout(() => {
        if (toast.parentElement) {
          toast.remove();
        }
      }, 5000);

      // Fade in animation
      toast.style.opacity = '0';
      toast.style.transform = 'translateX(100%)';
      setTimeout(() => {
        toast.style.transition = 'all 0.3s ease';
        toast.style.opacity = '1';
        toast.style.transform = 'translateX(0)';
      }, 100);
    },

    // Format currency (client-side display)
    formatCurrency: function (amount) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
      }).format(amount);
    },

    // Format date (client-side display)
    formatDate: function (date) {
      return new Intl.DateTimeFormat('vi-VN').format(new Date(date));
    },

    // Animate numbers (for statistics)
    animateNumber: function (element, target, duration = 2000) {
      const start = 0;
      const increment = target / (duration / 16);
      let current = start;

      const timer = setInterval(() => {
        current += increment;
        if (current >= target) {
          current = target;
          clearInterval(timer);
        }
        element.textContent = Math.floor(current);
      }, 16);
    },
  },
};

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function () {
  StaminaGym.init();

  // Animate statistics numbers when they come into view
  const statNumbers = document.querySelectorAll('.stat-number');
  if (statNumbers.length > 0) {
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          const target = parseInt(entry.target.textContent.replace(/\D/g, ''));
          if (target > 0) {
            StaminaGym.utils.animateNumber(entry.target, target);
            observer.unobserve(entry.target);
          }
        }
      });
    });

    statNumbers.forEach((num) => observer.observe(num));
  }
});

// Export for global use
window.StaminaGym = StaminaGym;
