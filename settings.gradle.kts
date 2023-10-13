pluginManagement {
    repositories {

        google()
        mavenCentral()
        gradlePluginPortal()
        maven ( "url https://developer.huawei.com/repo/" )
    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {

        google()
        mavenCentral()
        maven ( "https://developer.huawei.com/repo/" )
    }

}

rootProject.name = "WeatherForecastNew"
include(":app")
