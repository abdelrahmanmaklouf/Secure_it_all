// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ksp) apply false
    // FIX: Add this line to pull the alias from version catalogs
    alias(libs.plugins.compose.compiler) apply false
}