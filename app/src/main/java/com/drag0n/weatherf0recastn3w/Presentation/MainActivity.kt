package com.drag0n.weatherf0recastn3w.Presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.drag0n.weatherf0recastn3w.Const
import com.drag0n.weatherf0recastn3w.domane.Room.ItemCity
import com.drag0n.weatherf0recastn3w.DialogManager
import com.drag0n.weatherf0recastn3w.MainViewModel
import com.drag0n.weatherf0recastn3w.adapter.VpAdapter
import com.drag0n.weatherf0recastn3w.databinding.ActivityMainBinding
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.domane.Room.CityListDataBase
import com.drag0n.weatherf0recastn3w.adapter.ItemCityAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.common.AdRequest

class MainActivity : AppCompatActivity(), ItemCityAdapter.onClick { // Заканчивает MainActivity


    private lateinit var binding: ActivityMainBinding
    private lateinit var vpAdapter: VpAdapter
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var fLocotionClient: FusedLocationProviderClient
    private lateinit var fLocotionClientHMS: com.huawei.hms.location.FusedLocationProviderClient
    private lateinit var adapter: ItemCityAdapter
    private lateinit var db: CityListDataBase
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

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        fLocotionClient = LocationServices.getFusedLocationProviderClient(this)
        fLocotionClientHMS =
            com.huawei.hms.location.LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initDb()
        initVp()
        initRcView()
// яндекс реклама
        model.liveDataDayNow.observe(this) {
            when (it.weather[0].id) {
                200, 201, 202, 210, 211, 212, 221, 230, 231, 232 -> insertBackground(R.drawable.img_1)
                 // гроза
                300, 301, 302, 310, 311, 312, 313, 314, 321 -> insertBackground(R.drawable.img_2) // морось
                500, 501, 502, 503, 504, 511, 520, 521, 522, 531 -> insertBackground(R.drawable.img_3)
                 // дождь
                600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> insertBackground(R.drawable.img_4)
                 // снег
                701,711,721,741 -> insertBackground(R.drawable.img_7) // туман
                800 -> insertBackground(R.drawable.img_5) // Чистое небо
                else -> insertBackground(R.drawable.img_6)
            } // Меняет фон
        }
        db.CourseDao().getAll().asLiveData().observe(this){
            adapter.submitList(it)
        }

        with(binding) {
            imMenu.setOnClickListener{binding.drawer.openDrawer(GravityCompat.START)}
            bMyCity.setOnClickListener {
                chekLocation()
                binding.drawer.closeDrawer(GravityCompat.START)}
            bCallback.setOnClickListener {  try {
                binding.drawer.closeDrawer(GravityCompat.START)
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse( "mailto:apereverzev47@gmail.com" )))
            }  catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_SHORT).show()
            } }
            bUpdate.setOnClickListener {
                try {
                    binding.drawer.closeDrawer(GravityCompat.START)
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse( "https://apps.rustore.ru/app/com.drag0n.weatherf0recastn3w" )))
                }  catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_SHORT).show()
                }
            }
            imBAddMenu.setOnClickListener {
                DialogManager.nameSitySearchDialog(this@MainActivity, object : DialogManager.Listener {
                    override fun onClick(city: String?) {
                        Thread {
                            db.CourseDao().insertAll(ItemCity(null, city!!))
                        }.start()
                    }

                })
            }
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
            if (isHuaweiMobileServicesAvailable(this)) getLastLocationHuawey()
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

    private fun isHuaweiMobileServicesAvailable(context: Context): Boolean {
        val huaweiApiAvailability = HuaweiApiAvailability.getInstance()
        val resultCode = huaweiApiAvailability.isHuaweiMobileServicesAvailable(context)
        return resultCode == com.huawei.hms.api.ConnectionResult.SUCCESS
    }

     private fun getLastLocationHuawey() {
        fLocotionClientHMS.lastLocation.addOnSuccessListener {
            try {
                model.getGeoNew(it.latitude.toString(), it.longitude.toString(), this)
            } catch (e: NullPointerException){
                getLocationHuawey()
            }
        }

    } // Функция для получения последней геолокации Хуавея
    fun getLocationHuawey(){
       val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setNumUpdates(1)

       val callback = object : LocationCallback() {
            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
                Toast.makeText(
                    this@MainActivity,
                    "Ошибка при получении данных по геолокации, пожалуйста введите город вручную",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onLocationResult(it: LocationResult) {
                model.getGeoNew(it.lastLocation.latitude.toString(), it.lastLocation.longitude.toString(), this@MainActivity)
            }
        }
        fLocotionClientHMS.requestLocationUpdates(locationRequest, callback, null)
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
                try {
                    model.getGeoNew(it.result.latitude.toString(), it.result.longitude.toString(), this)
                }
                catch (_: Exception) {
                    Toast.makeText(
                        this,
                        "Ошибка при получении данных по геолокации, пожалуйста нажмите кнопку обновить или введите город вручную",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }


    } // Функция для получения геолокации Гугла

    private fun yaBaner() {
        binding.yaMob.setAdUnitId(Const.baner)
        binding.yaMob.setAdSize(BannerAdSize.stickySize(this, 350))
        val adRequest = AdRequest.Builder().build()
        binding.yaMob.loadAd(adRequest)
    } // яндекс банер
    private fun insertBackground(backgroud: Int){
        with(binding) {
            root.setBackgroundResource(backgroud)
        }


    }
    private fun initRcView() {
        val rcView = binding.rcView
        adapter = ItemCityAdapter(this)
        rcView.layoutManager = LinearLayoutManager(this)
        rcView.adapter = adapter

    } // инициализировал ресайклер
    private fun initDb(){
        db = Room.databaseBuilder(
            this,
            CityListDataBase::class.java, "CityList"
        ).build()
    }


    override fun onClick(itemCity: ItemCity, action: String) {
        when(action){
            Const.SEARCH_CITY-> {
                model.getApiNameCitiNow(itemCity.name, this)
                model.getApiNameCitiWeek(itemCity.name, this)
                binding.drawer.closeDrawer(GravityCompat.START)
            }
            Const.DELETE_CITY->{
                Thread{db.CourseDao().delete(itemCity)}.start()
            }
        }
    }


}



