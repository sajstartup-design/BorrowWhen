document.addEventListener("DOMContentLoaded", () => {
	const mobileBtn = document.getElementById('mobileMenuBtn');
    const mobileMenu = document.getElementById('mobileMenu');

    // Mobile toggle
    mobileBtn?.addEventListener('click', e => {
      e.stopPropagation();
      mobileMenu.classList.toggle('hidden');
    });

    // Dropdown toggles
    document.querySelectorAll('.dropdown-wrapper').forEach(wrapper => {
      const btn = wrapper.querySelector('.dropdown-btn');
      const menu = wrapper.querySelector('.dropdown-menu');

      btn?.addEventListener('click', e => {
        e.stopPropagation();
        document.querySelectorAll('.dropdown-menu').forEach(m => {
          if (m !== menu) m.classList.add('hidden');
        });
        menu?.classList.toggle('hidden');
		
		if (btn?.name === 'notification') {
	      console.log("🔔 Opening notifications dropdown");

	      // get the notification icon wrapper (the real DOM element)
	      const notificationIcon = document.querySelector('.notification-icon');
	      if (notificationIcon) {
	        notificationIcon.classList.remove('pulsating-bell', 'bell-ringing');
	      }
	    }
      });
    });

    // Global click to close all
    document.addEventListener('click', () => {
      document.querySelectorAll('.dropdown-menu').forEach(m => m.classList.add('hidden'));
      mobileMenu.classList.add('hidden');
    });
});

