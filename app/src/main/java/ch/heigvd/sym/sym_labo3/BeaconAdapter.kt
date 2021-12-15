package ch.heigvd.sym.sym_labo3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.altbeacon.beacon.Beacon
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Adapter for the recyclerview for the beacons
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
        val time = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now())

        holder.idTextView.text = context.getString(R.string.id, position + 1)
        holder.uuidTextView.text = context.getString(R.string.uuid, beacon.id1)
        holder.majorTextView.text = context.getString(R.string.major, beacon.id2)
        holder.minorTextView.text = context.getString(R.string.minor, beacon.id3)
        holder.rssiTextView.text = context.getString(R.string.rssi, beacon.rssi)
        holder.distanceTextView.text = context.getString(R.string.distance, String.format("%.3f", beacon.distance))
        holder.dateTextView.text = context.getString(R.string.last_detection_date, time)
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
