package com.example.myapplication

import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MainActivity : ComponentActivity() {

    private lateinit var locationClient: LocationClient
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    var locationList: ArrayList<Location> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ),
                0
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                0
            )
        }


    }

    override fun onStart() {
        super.onStart()


        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )


        locationClient
            .getLocationUpdates(5000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                /*val lat = location.latitude.toString()
                val long = location.longitude.toString()*/
                locationList.add(location)

                val time = Calendar.getInstance().time
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
                val current = formatter.format(time)
                setContent {
                    MyApplicationTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.error
                        ) {
                            LazyColumn {
                                items(locationList) { location ->
                                    ListItem(Pair(location, current))
                                }
                            }
                        }
                    }
                }
            }
            .launchIn(scope)

    }

    @Composable
    fun ListItem(data: Pair<Location, String>, modifier: Modifier = Modifier) {
        Row(modifier.fillMaxWidth()) {
            Text(text = "lat: " +data.first.latitude.toString() + " lon " + data.first.longitude.toString())
            Text(text = " current datetime: " +data.second)
            // … other composables required for displaying `data`
        }
    }

  /*  @Composable
    fun TestView(
    ) {
        var rssList by remember { mutableStateOf(emptyList<RssItems>()) }
        LaunchedEffect(Unit) {
            rssList = RssFetcher.fetchRss()
        }
        RssList(rssList)
    }*/

}




