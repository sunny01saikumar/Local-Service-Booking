# Google Play Store Deployment Guide for LocalHub

To deploy the **LocalHub Customer & Partner Mobile App** (which is a React web app built with Vite) to the Google Play Store, we must package it into a native Android package (`.apk` or `.aab`) using **CapacitorJS**.

---

## Phase 1: Package React as a Native Android App

Follow these steps on your local computer to convert the React web project into an Android Studio project:

### 1. Install Capacitor
Open your terminal in the **`frontend/mobile-app`** directory and install Capacitor's core CLI and Android platforms:
```bash
npm install @capacitor/core @capacitor/cli
npx cap init LocalHub com.localhub.app --web-dir=dist
```
* *(Note: When prompted for the app name, enter `LocalHub`. For the Package ID, enter `com.localhub.app`).*

### 2. Add Android Platform
Install the Android integration dependencies:
```bash
npm install @capacitor/android
npx cap add android
```

### 3. Build & Sync React Assets
Every time you make changes to your React frontend, you must build it and sync it into the Android project:
```bash
# Build the production Vite bundle (creates /dist folder)
npm run build

# Sync compiled HTML/JS/CSS assets to the Android Studio build directory
npx cap sync
```

---

## Phase 2: Generate Signed AAB (App Bundle) in Android Studio

Google Play Store requires an **Android App Bundle (.aab)** file signed with a secure upload key.

1. **Open the project in Android Studio**:
   ```bash
   npx cap open android
   ```
2. **Wait for Gradle Sync**: Allow Android Studio to download dependencies and sync files.
3. **Configure Keystore (Signing Key)**:
   * In Android Studio menu, click **`Build`** -> **`Generate Signed Bundle / APK...`**
   * Select **`Android App Bundle`** and click **`Next`**.
   * Click **`Create new...`** under *Key store path* to create a new secure keystore file (save this file safely!).
   * Fill out the keystore passwords and details.
4. **Generate Release Bundle**:
   * Select **`release`** as the build variant.
   * Click **`Create / Finish`**.
   * Android Studio will compile your code and generate your signed `.aab` file under `android/app/release/app-release.aab`.

---

## Phase 3: Publish to Google Play Console

To publish your app on the Google Play Store, you need a **Google Play Console Developer Account**.

### 1. Create a Developer Account
1. Go to the [Google Play Console](https://play.google.com/console/signup).
2. Sign in with your Google account.
3. Pay the **one-time $25 registration fee** (charged by Google to verify developer identity).
4. Complete your developer profile verification.

### 2. Set Up Your App Listing
1. Click **`Create app`** in the Play Console.
2. Enter your app details:
   * **App Name**: `LocalHub`
   * **Default Language**: English (or your preferred language)
   * **App or Game**: App
   * **Free or Paid**: Free
3. Fill out the **Store Presence** requirements:
   * **Short & Full Description**
   * **App Icon** (512x512 px PNG)
   * **Feature Graphic** (1024x500 px PNG)
   * **Phone Screen Screenshots** (Upload 2-4 screenshots of your React app running in mobile view)

### 3. Upload and Publish
1. Go to **`Production`** in the left sidebar menu under the *Release* tab.
2. Click **`Create new release`**.
3. Upload the signed **`app-release.aab`** file generated in Phase 2.
4. Fill out the release notes.
5. Click **`Save`**, then click **`Review release`**, and finally click **`Start roll-out to Production`**.

Google's review team will review your app (typically takes 2-4 days for new developers), and it will automatically go live on the Google Play Store once approved!
