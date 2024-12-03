# RETRO-App 

A Kotlin-based Android application designed for a dynamic and customizable shopping experience. Users can browse products, apply filters, and manage a shopping cart with an intuitive interface.

---

## Features

- **Dynamic Filtering System**  
  Filter products by price range, shirt sizes, and shoe sizes with real-time updates.
  
- **Interactive Product Grid**  
  A grid view displays products with images, prices, and selectable options.

- **Shopping Cart Management**  
  Add/remove items from the cart, view a summary, and reset the cart with animations.

- **Dark and Light Themes**  
  Toggle between dark mode and light mode dynamically.

- **Custom Dialogs**  
  Elegant confirmation dialogs for cart management.

---

## Technical Highlights

- **Modern UI Components**  
  - DrawerLayout for filters.  
  - GridView for product display.  
  - BottomSheetDialog for cart details.  
  - Custom styled dialogs.

- **Custom Adapter**  
  The `ImageAdapter` dynamically binds data to the grid view and handles user interactions.

- **Event-Driven Design**  
  Includes real-time updates for the total cart price and smooth animations.

- **Responsive Design**  
  Adjusts dynamically to various screen sizes.

---

## Code Structure

- **MainActivity**  
  Initializes the UI, manages interactions, and controls data flow.

- **ImageAdapter**  
  Custom adapter for binding product data and managing item interactions.

- **Data Model**  
  `ImageItem` represents product data such as images, prices, and sizes.

---

## How to Run

1. Clone the repository:  
   ```bash
   git clone <repository-url>
   cd <project-directory>
2. Open the project in Android Studio.
3. Sync Gradle files and ensure dependencies are installed.
4. Build and run on an emulator or device.
