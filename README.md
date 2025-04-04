# ScoutAI Android App

## Overview
ScoutAI is an Android app designed for capturing images along with their metadata (GPS coordinates and orientation). It utilizes the camera and sensors of an Android device to gather data in real-time, allowing users to capture images while recording location and orientation details. The app also includes functionality to display captured data in various formats and allows users to view the captured images in full-screen mode with zoom and pan capabilities.

## Getting Started

### 1. Clone the Repository
Clone the repository to your local machine using Git:

```bash
git clone https://github.com/Divyanshumalik1/ScoutAIapp.git
```

# ScoutAI Project Setup and Usage Guide

## Open the Project in Android Studio
1. Open Android Studio.
2. Click on **Open** and navigate to the folder where the project was cloned.
3. Select the **ScoutAI** project and open it in Android Studio.

## Run the App
1. Connect an Android device to your computer or start an Android emulator.
2. Click on the **Run** button in Android Studio (the green play icon) to build and run the app on the device or emulator.
3. Once the app launches, you will see the main screen where you can interact with the app by clicking the **"Get Started"** button.

## Permissions
Ensure that you grant the necessary permissions when prompted, including:
- **Camera Permission**: `Manifest.permission.CAMERA`
- **Location Permission**: `ACCESS_FINE_LOCATION` or `ACCESS_COARSE_LOCATION`
- **Orientation Sensor**: Access to read device orientation data.

## Dependencies
- **Jetpack Compose**: Used for building the UI with declarative components.
- **Android Navigation Component**: Handles navigation between the app’s screens.
- **Material3**: Provides UI components like buttons, text, and images, following Google’s Material Design guidelines.

## Core Components

### MainActivity.kt
The entry point of the app:
- Initializes the UI, applies the **ScoutAITheme**, and displays the **InitialScreen**.
- Clicking the **"Get Started"** button navigates to the **SecondScreen** using an **Intent**.
- Displays a full-screen background image with a centered logo and button.
- Clicking the **"Get Started"** button navigates to the next screen.

### GPSLocation.kt
Handles obtaining the device’s current GPS location using **FusedLocationProviderClient**:
- Requests the necessary permissions and fetches the last known location.

### Orientation.kt
Tracks the device’s orientation using the accelerometer and magnetometer:
- Provides the device’s **azimuth**, **pitch**, and **roll** values in real-time.

### SecondScreen.kt
Displays instructions for using the app with a green background, and includes a **"Next"** button that navigates to the **FunctionalActivity**.

### FullscreenImageDialog.kt
Displays an image in full-screen mode with zoom and pan functionality.

### CameraViewModelAct.kt
Manages camera-related state:
- Handles permissions, image capture, and updates the state of captured images with metadata (location and orientation).

### BottomNavigation.kt
Defines the bottom navigation bar for switching between different sections of the app:
- Navigation items include **"Camera"** and **"Data"**, with corresponding icons for each.

## Data Collection and Visualization

### DataVis.kt
Displays a list of captured images and their metadata:
- Allows toggling between list and table view for image metadata.
- Includes a full-screen view for captured images with zoom and pan features.

### CapturedTextData.kt
Displays a table of image metadata (azimuth, pitch, roll, latitude, longitude):
- Allows users to download image metadata as a text file for offline use.

### CameraFunctionality.kt
Captures images using **CameraX** while also collecting GPS and orientation data:
- Periodically captures images based on a defined interval.
- Tracks and stores metadata along with the images.

## Assumptions, Limitations, and Known Issues

### Assumptions:
- The app assumes that the camera is fixed in a vertical position during image capture. It is currently configured for capturing images in a vertical orientation, and tilting the camera may cause issues with metadata accuracy.

### Known Issues:
- If the capture interval is set too low, multiple images may be captured in rapid succession and added to the **ItemsList**, potentially leading to redundant data.

## Requirements
To run this app, ensure that the following permissions are granted:
- **Camera Permission**: `Manifest.permission.CAMERA`
- **Location Permission**: `ACCESS_FINE_LOCATION` or `ACCESS_COARSE_LOCATION`
- **Orientation Sensor Access**: Permissions to access the device’s orientation sensors.
