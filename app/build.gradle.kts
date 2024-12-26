plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.annisa.sisteminformasisekolah"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.annisa.sisteminformasisekolah"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // Core AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material) // Material Design Components
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Retrofit for Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp3 for Logging Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0") // Versi terbaru

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // Unit Testing
    testImplementation(libs.junit)

    // Android Instrumentation Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Optional - Lifecycle Components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Versi terbaru
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Optional - RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2") // Versi terbaru

    //Picasso
    implementation ("com.squareup.picasso:picasso:2.8")
}
