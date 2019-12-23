package kdo.one.roomappkotlin.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kdo.one.roomappkotlin.R
import kdo.one.roomappkotlin.db.Note
import kotlinx.android.synthetic.main.note_layout.view.*

class NoteAdapter(
    private val list: List<Note>
): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.note_layout, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.view.text_view_title.text = list[position].title
        holder.view.text_view_note.text = list[position].note

        holder.view.setOnClickListener {
            val action = HomeFragmentDirections.actionAddNote()
            action.note = list[position]
            Navigation.findNavController(it).navigate(action)
        }
    }
}