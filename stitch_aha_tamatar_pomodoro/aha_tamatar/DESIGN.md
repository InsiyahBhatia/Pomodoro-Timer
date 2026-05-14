---
name: Aha Tamatar
colors:
  surface: '#fbf9f1'
  surface-dim: '#dcdad2'
  surface-bright: '#fbf9f1'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f5f4ec'
  surface-container: '#f0eee6'
  surface-container-high: '#eae8e0'
  surface-container-highest: '#e4e3db'
  on-surface: '#1b1c17'
  on-surface-variant: '#5a413c'
  inverse-surface: '#30312c'
  inverse-on-surface: '#f3f1e9'
  outline: '#8e706b'
  outline-variant: '#e2bfb8'
  surface-tint: '#b12c16'
  primary: '#b12c16'
  on-primary: '#ffffff'
  primary-container: '#ff6347'
  on-primary-container: '#630900'
  inverse-primary: '#ffb4a5'
  secondary: '#006e0a'
  on-secondary: '#ffffff'
  secondary-container: '#69fd5d'
  on-secondary-container: '#00730b'
  tertiary: '#705d00'
  on-tertiary: '#ffffff'
  tertiary-container: '#c9a900'
  on-tertiary-container: '#4c3f00'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffdad3'
  primary-fixed-dim: '#ffb4a5'
  on-primary-fixed: '#3f0400'
  on-primary-fixed-variant: '#8f1100'
  secondary-fixed: '#75ff68'
  secondary-fixed-dim: '#4ce346'
  on-secondary-fixed: '#002201'
  on-secondary-fixed-variant: '#005306'
  tertiary-fixed: '#ffe16d'
  tertiary-fixed-dim: '#e9c400'
  on-tertiary-fixed: '#221b00'
  on-tertiary-fixed-variant: '#544600'
  background: '#fbf9f1'
  on-background: '#1b1c17'
  surface-variant: '#e4e3db'
typography:
  display-timer:
    fontFamily: Quicksand
    fontSize: 80px
    fontWeight: '700'
    lineHeight: 90px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Quicksand
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Quicksand
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
  headline-md:
    fontFamily: Quicksand
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  body-lg:
    fontFamily: Quicksand
    fontSize: 18px
    fontWeight: '500'
    lineHeight: 28px
  body-md:
    fontFamily: Quicksand
    fontSize: 16px
    fontWeight: '500'
    lineHeight: 24px
  label-md:
    fontFamily: Quicksand
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.05em
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  base: 8px
  xs: 4px
  sm: 12px
  md: 24px
  lg: 40px
  xl: 64px
  gutter: 16px
  margin-mobile: 20px
  margin-desktop: auto
---

## Brand & Style
The brand personality is exuberant, rhythmic, and nurturing. Designed primarily for children and students, this design system evokes the catchy, repetitive joy of nursery rhymes to transform time management into a playful activity. The emotional response is one of encouragement and "bouncy" energy rather than rigid productivity.

The visual style is a blend of **Minimalism** and **Tactile/Skeuomorphism**. By utilizing clean, spacious layouts paired with "squishy," toy-like interactive elements, the UI feels both modern and physically relatable. The interface avoids complexity, favoring large touch targets and high-contrast visual cues that guide the user through their focus and break cycles with the friendly charm of a garden.

## Colors
The palette is derived from a sun-drenched garden. The primary **Tomato Red** is used for the most active states and the main Pomodoro timer, signaling energy and focus. The **Leaf Green** serves as the "Rest" or "Success" color, providing a calming yet vibrant contrast during breaks. **Sunny Yellow** acts as an accent for highlights, stars, and celebratory animations.

The background uses a warm, creamy neutral rather than a stark white to reduce eye strain and maintain a soft, inviting atmosphere. All colors are high-saturation to maintain a "kid-friendly" vibrancy that feels alive and interactive.

## Typography
This design system utilizes **Quicksand** exclusively to leverage its rounded terminals and open counters, which feel inherently friendly and legible for younger audiences. 

The typographic hierarchy is topped by a massive `display-timer` style, ensuring the countdown is the undisputed focal point of the experience. Headlines are bold and chunky to maintain the playful aesthetic, while body text remains medium-weight to ensure clarity against vibrant background colors. All type should be rendered with slightly increased leading (line height) to feel airy and approachable.

## Layout & Spacing
The layout follows a **fluid grid** model that prioritizes vertical rhythm and ease of interaction. On mobile, elements span the full width minus a generous 20px margin, ensuring that buttons are easy for smaller hands to tap.

The spacing rhythm is built on an 8px base, but it leans toward larger increments (`md` and `lg`) to prevent the UI from feeling cluttered. Content should be centered vertically and horizontally in the viewport to maintain a focused, "app-like" feel even on larger screens. The layout adapts by increasing side margins on desktop to keep the central container at a maximum width of 480px, preserving the intimacy of the mobile experience.

## Elevation & Depth
Depth in this design system is achieved through **Tonal Layers** and **Ambient Shadows** that avoid traditional "web" aesthetics in favor of a tactile, physical look. 

Surfaces do not use harsh black shadows; instead, they use soft, diffused shadows tinted with the color of the element (e.g., a Tomato Red button casts a soft, dark-red shadow). This creates a "glow" effect that feels more organic and less digital. Interactive elements use a double-layered shadow to appear slightly extruded from the background, suggesting they are "pressable" like a physical toy. When an element is active or pressed, the shadow should shrink and the element should scale down slightly (98%) to simulate physical compression.

## Shapes
The shape language is dominated by **Pill-shaped (Level 3)** geometry. There are no sharp corners in the entire design system. 

Every container, button, and input field utilizes a full corner radius to mimic the organic shape of a tomato. This extreme roundedness removes any sense of "edge" or "danger," reinforcing the safe and friendly environment. Secondary containers, like cards for task lists, use a `rounded-xl` setting (1.5rem to 3rem) to maintain consistency with the primary pill shapes while allowing for more internal content structure.

## Components
- **Buttons:** These are the primary interactive units. They are large, pill-shaped, and use the Primary Red or Secondary Green. They feature a 4px bottom "lip" (a slightly darker shade of the button color) to create a 3D effect.
- **The Tomato Timer:** A large circular or tomato-shaped progress indicator that fills with red as the time passes. The countdown text sits inside the center using the `display-timer` style.
- **Chips:** Used for "Task Tags" (e.g., Homework, Reading). These are smaller pill shapes with Sunny Yellow backgrounds and dark brown or deep red text for high legibility.
- **Input Fields:** These have thick, soft borders (2px) in a muted version of the Leaf Green. The focus state should "pop" with a Sunny Yellow glow.
- **Cards:** Used for setting up the Pomodoro session. They feature subtle, tinted shadows and large internal padding (`md` or 24px) to keep the information feeling light and uncrowded.
- **Checkboxes:** Styled as small, round "seeds" that turn into a bright green checkmark when tapped, accompanied by a slight "bounce" animation.