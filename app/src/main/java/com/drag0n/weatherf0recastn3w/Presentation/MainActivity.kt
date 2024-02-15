package com.drag0n.weatherf0recastn3w.Presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.drag0n.weatherf0recastn3w.Const
import com.drag0n.weatherf0recastn3w.DialogManager
import com.drag0n.weatherf0recastn3w.MainViewModel
import com.drag0n.weatherf0recastn3w.adapter.VpAdapter
import com.drag0n.weatherf0recastn3w.databinding.ActivityMainBinding
import com.drag0n.weatherf0recastn3w.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.huawei.hms.api.HuaweiApiAvailability
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.common.AdRequest

class MainActivity : AppCompatActivity() { // Заканчивает MainActivity


    private lateinit var binding: ActivityMainBinding
    private lateinit var vpAdapter: VpAdapter
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var fLocotionClient: FusedLocationProviderClient
    private lateinit var fLocotionClientHMS: com.huawei.hms.location.FusedLocationProviderClient
    private val model: MainViewModel by viewModels()
    private val listFrag = listOf(
        FragmentDay.newInstance(),
        FragmentWeek.newInstance(),
        FragmentMap.newInstance()
    )
    private val listName = listOf(
        "Погода на день",
        "Погода на 5 дней",
        "Карта погоды"
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {


        binding = ActivityMainBinding.inflate(layoutInflater)
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        fLocotionClient = LocationServices.getFusedLocationProviderClient(this)
        fLocotionClientHMS =
            com.huawei.hms.location.LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initVp()
// яндекс реклама
        model.liveDataDayNow.observe(this) {
            when (it.weather[0].id) {
                200, 201, 202, 210, 211, 212, 221, 230, 231, 232 -> binding.root.setBackgroundResource(
                    R.drawable.img_1
                ) // гроза
                300, 301, 302, 310, 311, 312, 313, 314, 321 -> binding.root.setBackgroundResource(R.drawable.img_2) // морось
                500, 501, 502, 503, 504, 511, 520, 521, 522, 531 -> binding.root.setBackgroundResource(
                    R.drawable.img_3
                ) // дождь
                600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> binding.root.setBackgroundResource(
                    R.drawable.img_4
                ) // снег
                800 -> binding.root.setBackgroundResource(R.drawable.img_5) // Чистое небо
                else -> binding.root.setBackgroundResource(R.drawable.img_6)
            } // Меняет фон
        }

    } // OnCreate

    override fun onResume() {
        super.onResume()

        chekPermissionLocation()
        yaBaner()

    }

    private fun initVp() {
        vpAdapter = VpAdapter(this, listFrag)
        binding.placeHolder.adapter = vpAdapter
        TabLayoutMediator(binding.tabLayout, binding.placeHolder) { tab, pos ->
            tab.text = listName[pos]
        }.attach()
    } // инициализирую ViewPager

     fun chekPermissionLocation() {
        if (Const.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            chekLocation()
        } else {
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } // проверяет есть ли разрешение геолокации

     fun chekLocation() {
        if (isLocationEnabled()) {
            if (isHuaweiMobileServicesAvailable(this)) getLocationHuawey()
            else getLocationGoogle()
        } else {
            DialogManager.locationSettingsDialog(this, object : DialogManager.Listener {
                override fun onClick(city: String?) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

            })
        }
    } // Функция проверяет включено ли GPS

    private fun isLocationEnabled(): Boolean {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } // Функция узнает включено ли GPS

    fun isHuaweiMobileServicesAvailable(context: Context): Boolean {
        val huaweiApiAvailability = HuaweiApiAvailability.getInstance()
        val resultCode = huaweiApiAvailability.isHuaweiMobileServicesAvailable(context)
        return resultCode == com.huawei.hms.api.ConnectionResult.SUCCESS
    }

    private fun getLocationHuawey() {

        fLocotionClientHMS.lastLocation.addOnSuccessListener {
            try {
                Log.d("MyLog", "Успешно")
                model.getGeoNew(it.latitude.toString(), it.longitude.toString(), this)
            } catch (_: Exception) {
                Log.d("MyLog", "Не успешно")
                Toast.makeText(
                    this,
                    "Ошибка при получении данных по геолокации, пожалуйста нажмите кнопку обновить или введите город вручную",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    } // Функция для получения геолокации Хуавея

    private fun getLocationGoogle() {
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocotionClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                model.getGeoNew(it.result.latitude.toString(), it.result.longitude.toString(), this)
            }


    } // Функция для получения геолокации Гугла

    private fun yaBaner() {
        binding.yaMob.setAdUnitId(Const.baner)
        binding.yaMob.setAdSize(BannerAdSize.stickySize(this, 350))
        val adRequest = AdRequest.Builder().build()
        binding.yaMob.loadAd(adRequest)
    } // яндекс банер


}

