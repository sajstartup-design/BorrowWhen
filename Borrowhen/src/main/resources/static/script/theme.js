(function () {
      try {
        const root = document.documentElement;
        const layoutPath = root.getAttribute('data-layout-path')?.replace('https://demos.flyonui.com/', '') || 'dashboard-default';
        const localStorageKey = `${layoutPath}-theme`;

        // Theme configuration loaded from page-config.json at build time
        window.THEME_CONFIG = {"dashboard-ecommerce":{"default":"corporate","light":"corporate","dark":"corporate-dark","system":{"light":"corporate","dark":"corporate-dark"}}};

        // Get current system theme preference
        const getSystemPreference = () => window.matchMedia('(prefers-color-scheme: dark)').matches;

        // Resolve theme based on user selection and layout configuration
        const resolveTheme = (selectedTheme, layoutPath) => {
          const layoutConfig = window.THEME_CONFIG[layoutPath];
          if (!layoutConfig) return selectedTheme === 'system' ?
            (getSystemPreference() ? 'dark' : 'light') : selectedTheme;

          if (selectedTheme === 'system') {
            const systemConfig = layoutConfig.system;
            const prefersDark = getSystemPreference();
            return prefersDark ? systemConfig.dark : systemConfig.light;
          }

          return layoutConfig[selectedTheme] || selectedTheme || layoutConfig.default || 'light';
        };

        const savedTheme = localStorage.getItem(localStorageKey) || 'system';
        const resolvedTheme = resolveTheme(savedTheme, layoutPath);

        root.setAttribute('data-theme', resolvedTheme);
      } catch (e) {
        console.warn('Early theme script error:', e);
      }
    }) ();