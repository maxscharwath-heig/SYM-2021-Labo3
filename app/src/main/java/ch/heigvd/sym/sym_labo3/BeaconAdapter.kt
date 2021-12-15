package ch.heigvd.sym.sym_labo3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.altbeacon.beacon.Beacon
import java.time.LocalDateTime

/**
 * adapter for the recyclerview for the beacons
 */
class BeaconAdapter(private val beaconList: List<Beacon>, private val context : Context) :
    RecyclerView.Adapter<BeaconAdapter.ViewHolder>() {

    //When we create a viewHolder we inflate it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.beacon, parent, false)
        return ViewHolder(view)
    }

    //Binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beacon = beaconList[position]
        val time = LocalDateTime.now()
        //Here using string builders seemed more readable
        holder.idTextView.text = buildString {
            append(context.getString(R.string.id))
            append(" ")
            append(position + 1)
        }
        holder.uuidTextView.text = buildString {
            append(context.getString(R.string.uuid))
            append(" ")
            append(beacon.id1)
        }
        holder.majorTextView.text = buildString {
            append(context.getString(R.string.major))
            append(" ")
            append(beacon.id2)
        }
        holder.minorTextView.text = buildString {
            append(context.getString(R.string.minor))
            append(" ")
            append(beacon.id3)
        }
        holder.rssiTextView.text = buildString {
            append(context.getString(R.string.rssi))
            append(" ")
            append(beacon.rssi)
        }
        holder.distanceTextView.text = buildString {
            append(context.getString(R.string.distance))
            append(" ")
            append(String.format("%.3f", beacon.distance))
            append(" m")
        }
        holder.dateTextView.text = buildString {
            append(context.getString(R.string.last_detection_date))
            append(" ")
            append(time.dayOfMonth)
            append("-")
            append(time.monthValue)
            append("-")
            append(time.year)
            append("  ")
            append(time.hour)
            append(":")
            append(time.minute)
            append(":")
            append(time.second)
        }
    }

    //Return the number of the items in the list
    override fun getItemCount(): Int {
        return beaconList.size
    }

    //Holds the views for a specific beacon
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val idTextView: TextView = itemView.findViewById(R.id.id)
        val uuidTextView: TextView = itemView.findViewById(R.id.UUID)
        val majorTextView: TextView = itemView.findViewById(R.id.major)
        val minorTextView: TextView = itemView.findViewById(R.id.minor)
        val rssiTextView: TextView = itemView.findViewById(R.id.rssi)
        val distanceTextView: TextView = itemView.findViewById(R.id.distance)
        val dateTextView: TextView = itemView.findViewById(R.id.date)

    }
}
