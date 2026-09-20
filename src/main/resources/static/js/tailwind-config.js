tailwind.config = {
    darkMode: "class",
    theme: {
        extend: {
            colors: {
                "background": "#0a0a0a",
                "on-background": "#f5f5f5",
                "surface-container-lowest": "#000000",
                "surface-container-low": "#121212",
                "surface-container": "#1a1a1a",
                "surface-container-high": "#222222",
                "surface-container-highest": "#2a2a2a",
                "surface": "#141414",
                "surface-dim": "#0f0f0f",
                "surface-bright": "#262626",
                "surface-variant": "#2a2a2a",

                "on-surface": "#ffffff",
                "on-surface-variant": "#a3a3a3",
                "inverse-surface": "#e4e2e1",
                "inverse-on-surface": "#303030",
                "secondary-text": "#a3a3a3",

                "outline": "#404040",
                "outline-variant": "#262626",

                "primary": "#e31c25",
                "on-primary": "#ffffff",
                "primary-container": "#ac0101",
                "on-primary-container": "#ffffff",
                "inverse-primary": "#ff6666",
                "primary-hover": "#990000",
                "surface-tint": "#ff3333",


                "secondary": "#525252",
                "on-secondary": "#ffffff",
                "secondary-container": "#333333",
                "on-secondary-container": "#cccccc",

                "tertiary": "#737373",
                "on-tertiary": "#ffffff",
                "tertiary-container": "#404040",
                "on-tertiary-container": "#e5e5e5",

                "error": "#ff5555",
                "on-error": "#ffffff",
                "error-container": "#550000",
                "on-error-container": "#ffdad6",

                "primary-fixed": "#ffdad6",
                "primary-fixed-dim": "#ffb4ac",
                "on-primary-fixed": "#410003",
                "on-primary-fixed-variant": "#93000e",

                "secondary-fixed": "#e5e2e1",
                "secondary-fixed-dim": "#c8c6c5",
                "on-secondary-fixed": "#1c1b1b",
                "on-secondary-fixed-variant": "#474746",

                "tertiary-fixed": "#e5e2e1",
                "tertiary-fixed-dim": "#c8c6c5",
                "on-tertiary-fixed": "#1c1b1b",
                "on-tertiary-fixed-variant": "#474646"
            },

            borderRadius: {
                "DEFAULT": "0.5rem",
                "lg": "0.5rem",
                "xl": "0.75rem",
                "full": "9999px"
            },

            spacing: {
                "container-max": "1280px",
                "stack-lg": "48px",
                "stack-md": "24px",
                "base": "8px",
                "margin-mobile": "16px",
                "gutter": "24px",
                "margin-desktop": "40px",
                "stack-sm": "12px"
            },

            fontFamily: {
                "headline-lg": ["Archivo Narrow", "sans-serif"],
                "headline-md": ["Archivo Narrow", "sans-serif"],
                "body-lg": ["Inter", "sans-serif"],
                "stats-xl": ["Geist", "sans-serif"],
                "headline-lg-mobile": ["Archivo Narrow", "sans-serif"],
                "display-lg": ["Archivo Narrow", "sans-serif"],
                "label-md": ["Geist", "sans-serif"],
                "body-md": ["Inter", "sans-serif"]
            },

            fontSize: {
                "headline-lg": ["32px", {"lineHeight": "1.2", "letterSpacing": "-0.01em", "fontWeight": "700"}],
                "headline-md": ["24px", {"lineHeight": "1.2", "fontWeight": "600"}],
                "body-lg": ["18px", {"lineHeight": "1.6", "fontWeight": "400"}],
                "stats-xl": ["40px", {"lineHeight": "1.0", "letterSpacing": "-0.04em", "fontWeight": "700"}],
                "headline-lg-mobile": ["28px", {"lineHeight": "1.2", "fontWeight": "700"}],
                "display-lg": ["48px", {"lineHeight": "1.1", "letterSpacing": "-0.02em", "fontWeight": "700"}],
                "label-md": ["14px", {"lineHeight": "1.0", "letterSpacing": "0.05em", "fontWeight": "500"}],
                "body-md": ["16px", {"lineHeight": "1.5", "fontWeight": "400"}]
            }
        }
    }
};