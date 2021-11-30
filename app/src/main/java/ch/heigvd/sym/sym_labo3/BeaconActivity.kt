package ch.heigvd.sym.sym_labo3

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import org.altbeacon.beacon.*





class BeaconActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_beacon)

        val beaconsView = findViewById<RecyclerView>(R.id.beacons)

        val beaconParser = BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")

        val beaconManager =  BeaconManager.getInstanceForApplication(this)
        val region = Region("all-beacons-region", null, null, null)
        // Set up a Live Data observer so this Activity can get ranging callbacks
        // observer will be called each time the monitored regionState changes (inside vs. outside region)
        beaconManager.getRegionViewModel(region).rangedBeacons.observe(this, rangingObserver)
        beaconManager.startRangingBeacons(region)
    }


    val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(TAG, "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            Log.d(TAG, "$beacon about ${beacon.distance} meters away")

        }
    }

    // Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
    class beaconAdapter : RecyclerView.Adapter<beaconAdapter.ViewHolder>() {

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // Your holder should contain and initialize a member variable
            // for any view that will be set as you render a row
            val majorTextView = itemView.findViewById<TextView>(R.id.major)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }
    }
}