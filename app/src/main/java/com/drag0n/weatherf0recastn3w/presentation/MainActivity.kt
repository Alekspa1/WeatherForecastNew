package com.drag0n.weatherf0recastn3w.presentation

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.common.AdRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.RuStoreBillingClientFactory
import ru.rustore.sdk.billingclient.model.purchase.PaymentResult
import ru.rustore.sdk.billingclient.model.purchase.Purchase
import ru.rustore.sdk.billingclient.model.purchase.PurchaseState
import ru.rustore.sdk.billingclient.usecase.ProductsUseCase
import ru.rustore.sdk.billingclient.usecase.PurchasesUseCase
import ru.rustore.sdk.billingclient.utils.pub.checkPurchasesAvailability
import ru.rustore.sdk.core.feature.model.FeatureAvailabilityResult
import java.util.UUID


class MainActivity : AppCompatActivity(), ItemCityAdapter.onClick {
    lateinit var binding: ActivityMainBinding
    private lateinit var vpAdapter: VpAdapter
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var fLocotionClient: FusedLocationProviderClient
    private lateinit var fLocotionClientHMS: com.huawei.hms.location.FusedLocationProviderClient
    private lateinit var adapter: ItemCityAdapter
    private lateinit var db: CityListDataBase
    private val model: MainViewModel by viewModels()

    private lateinit var billingClient: RuStoreBillingClient
    private lateinit var productsUseCase: ProductsUseCase
    private lateinit var purchasesUseCase: PurchasesUseCase
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        initAll()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            billingClient.onNewIntent(intent)
        }
        if(!(pref.getBoolean(Const.premium_KEY, false))) yaBaner()
        Log.d("MyLog",(pref.getBoolean(Const.premium_KEY, false).toString()))
        val calendar = Calendar.getInstance().timeInMillis

        model.load.observe(this){
            if (model.load.value == true) binding.progressBar2.visibility = View.VISIBLE
            else binding.progressBar2.visibility = View.GONE
        }
        model.liveDataDayNow.observe(this) {
            model.load.value = false
            val rassvet = it.sys.sunrise * 1000L
            val zakat = (it.sys.sunset * 1000L) + AlarmManager.INTERVAL_HALF_HOUR

            if (calendar > zakat || calendar < rassvet)
                when (it.weather[0].id) {
                    200, 201, 202, 210, 211, 212, 221, 230, 231, 232 -> insertBackground(R.drawable.img_1_night)
                    // гроза
                    300, 301, 302, 310, 311, 312, 313, 314, 321 -> insertBackground(R.drawable.img_2_night) // морось
                    500, 501, 502, 503, 504, 511, 520, 521, 522, 531 -> insertBackground(R.drawable.img_3_night)
                    // дождь
                    600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> insertBackground(R.drawable.img_4_night)
                    // снег
                    701, 711, 721, 741 -> insertBackground(R.drawable.img_5_night) // туман
                    800 -> insertBackground(R.drawable.img_night) // Чистое небо
                    else -> insertBackground(R.drawable.img_8)
                } // Меняет фон вечером
            else {
                when (it.weather[0].id) {
                    200, 201, 202, 210, 211, 212, 221, 230, 231, 232 -> insertBackground(R.drawable.img_1_day)
                    // гроза
                    300, 301, 302, 310, 311, 312, 313, 314, 321 -> insertBackground(R.drawable.img_2_day) // морось
                    500, 501, 502, 503, 504, 511, 520, 521, 522, 531 -> insertBackground(R.drawable.img_3_day)
                    // дождь
                    600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> insertBackground(R.drawable.img_4_day)
                    // снег
                    701, 711, 721, 741 -> insertBackground(R.drawable.img_5_day) // туман
                    800 -> insertBackground(R.drawable.img) // Чистое небо
                    else -> insertBackground(R.drawable.img_6)
                } // Меняет фон днем
            }
        }

        with(binding) {
            imMenu.setOnClickListener { binding.drawer.openDrawer(GravityCompat.START) }
            bMyCity.setOnClickListener {
                model.load.value = true
                chekLocation()
                binding.drawer.closeDrawer(GravityCompat.START)
            }
            bCallback.setOnClickListener {
                try {
                    binding.drawer.closeDrawer(GravityCompat.START)
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("mailto:apereverzev47@gmail.com")
                        )
                    )
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, getString(R.string.main_error), Toast.LENGTH_SHORT).show()
                }
            }
            bUpdate.setOnClickListener {
                TODO("МАГАЗИН")
                try {
                    binding.drawer.closeDrawer(GravityCompat.START)
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://apps.rustore.ru/app/com.drag0n.weatherf0recastn3w")
                        )
                    )
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, getString(R.string.main_error), Toast.LENGTH_SHORT).show()
                }
            }
            imBAddMenu.setOnClickListener {
                DialogManager.nameSitySearchDialog(
                    this@MainActivity,
                    object : DialogManager.Listener {
                        override fun onClick(city: String?) {
                            if (city!!.isNotEmpty()) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    db.CourseDao().insertAll(ItemCity(null, city))
                                }
                            } else Toast.makeText(
                                this@MainActivity,
                                getString(R.string.main_isNotEmpty),
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    })
            }
            bPremium.setOnClickListener {
                proverkaVozmoznoyOplaty(this@MainActivity)
                drawer.closeDrawer(GravityCompat.START)
            }
        }


    } // OnCreate

    override fun onResume() {
        super.onResume()
        chekPermissionLocation()
        model.load.value = true
        shopingList()
    }


    private fun chekPermissionLocation() {
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
            } catch (e: NullPointerException) {
                getLocationHuawey()
            }
        }

    } // Функция для получения последней геолокации Хуавея

    private fun getLocationHuawey() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setNumUpdates(1)

        val callback = object : LocationCallback() {
            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.main_error_geo),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onLocationResult(it: LocationResult) {
                model.getGeoNew(
                    it.lastLocation.latitude.toString(),
                    it.lastLocation.longitude.toString(),
                    this@MainActivity
                )
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
                    model.getGeoNew(
                        it.result.latitude.toString(),
                        it.result.longitude.toString(),
                        this@MainActivity
                    )
                } catch (_: Exception) {
                    Toast.makeText(
                        this,
                        getString(R.string.main_error_geo),
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

    private fun insertBackground(backgroud: Int) {
        with(binding) {
            root.setBackgroundResource(backgroud)
        }


    }





    override fun onClick(itemCity: ItemCity, action: String) {
        when (action) {
            Const.SEARCH_CITY -> {
                model.load.value = true
                model.getApiNameCitiNow(itemCity.name, this)
                model.getApiNameCitiWeek(itemCity.name, this)
                binding.drawer.closeDrawer(GravityCompat.START)
            }

            Const.DELETE_CITY -> CoroutineScope(Dispatchers.IO).launch {
                db.CourseDao().delete(itemCity)
            }
        }

    }


    private fun proverkaVozmoznoyOplaty(context: Context) {
        RuStoreBillingClient.checkPurchasesAvailability(context)
            .addOnSuccessListener { result ->
                when (result) {
                    FeatureAvailabilityResult.Available -> {
                        pokupka()
                    }
                    is FeatureAvailabilityResult.Unavailable -> {
                        Log.d("MyLog", result.cause.toString())
                        Toast.makeText(context, "Оплата временно недоступна", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }.addOnFailureListener {

            }
    } // Проверка возможности оплатить
    private fun pokupka() {
        purchasesUseCase.purchaseProduct(
            productId = "premium_version_weather_forecast",
            orderId = UUID.randomUUID().toString(),
            quantity = 1,
            developerPayload = null,
        ).addOnSuccessListener { paymentResult: PaymentResult ->
            when (paymentResult) {
                is PaymentResult.Success -> {
                    edit.putBoolean(Const.premium_KEY, true)
                    edit.apply()
                    Toast.makeText(
                        this,
                        "Поздравляю! Теперь у вас не будет рекламы",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    Toast.makeText(
                        this,
                        "Произошла ошибка оплаты",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.addOnFailureListener {
        }
    } // Покупка товара
    private fun shopingList() {
        purchasesUseCase.getPurchases()
            .addOnSuccessListener { purchases: List<Purchase> ->
                if (purchases.isEmpty() && (pref.getBoolean(Const.premium_KEY, false))) {
                    edit.putBoolean(Const.premium_KEY, false)
                    edit.apply()
                }
                purchases.forEach {
                    if (it.productId == "premium_version_weather_forecast" &&
                        (it.purchaseState == PurchaseState.PAID ||
                                it.purchaseState == PurchaseState.CONFIRMED) && !(pref.getBoolean(Const.premium_KEY, false)))
                     {
                        edit.putBoolean(Const.premium_KEY, true)
                        edit.apply()
                    }
                }

            }
            .addOnFailureListener {
                // Process error
            }

    } // Запрос ранее совершенных покупок
    private fun initAll(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        fLocotionClient = LocationServices.getFusedLocationProviderClient(this)
        fLocotionClientHMS =
            com.huawei.hms.location.LocationServices.getFusedLocationProviderClient(this)

        billingClient = RuStoreBillingClientFactory.create(
            context = this,
            consoleApplicationId = "2063502125",
            deeplinkScheme = "yourappscheme"
        )
        productsUseCase = billingClient.products
        purchasesUseCase = billingClient.purchases
        pref = this.getSharedPreferences("PREMIUM", Context.MODE_PRIVATE)
        edit = pref.edit()

        db = Room.databaseBuilder(
            this,
            CityListDataBase::class.java, "CityList"
        ).build()


        vpAdapter = VpAdapter(this)
        binding.placeHolder.adapter = vpAdapter
        TabLayoutMediator(binding.tabLayout, binding.placeHolder) { tab, pos ->
            tab.text = resources.getStringArray(R.array.vp_title_main)[pos]
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.placeHolder.isUserInputEnabled = tab.position != 2
                if(tab.position == 2) binding.yaMob.visibility = View.GONE
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        val rcView = binding.rcView
        adapter = ItemCityAdapter(this)
        rcView.layoutManager = LinearLayoutManager(this)
        rcView.adapter = adapter
        db.CourseDao().getAll().asLiveData().observe(this) {
            adapter.submitList(it)
        }
    }
}



