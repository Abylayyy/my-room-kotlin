package kdo.one.roomappkotlin.ui


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import kdo.one.roomappkotlin.R
import kdo.one.roomappkotlin.db.Note
import kdo.one.roomappkotlin.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch

class AddNoteFragment : BaseFragment() {

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            et_title.setText(note?.title)
            et_note.setText(note?.note)
        }

        button_save.setOnClickListener {view ->
            val title = et_title.text.toString().trim()
            val notetxt = et_note.text.toString().trim()

            if (title.isEmpty()) {
                et_title.error = "Title required"
                et_title.requestFocus()
                return@setOnClickListener
            }
            if (notetxt.isEmpty()) {
                et_note.error = "Note required"
                et_note.requestFocus()
                return@setOnClickListener
            }

            launch {
                context?.let {
                    val note1 = Note(title, notetxt)
                    if (note == null) {
                        NoteDatabase(it).getNoteDao().addNote(note1)
                        it.toast("Note Saved!")
                    } else {
                        note1.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(note1)
                        it.toast("Note Updated!")
                    }
                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete_note -> {
                if (note != null) {
                    deleteNote()
                }
            }
            else -> {
                context?.toast("Cannot delete the note")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNote() {
        AlertDialog.Builder(context!!).apply {
            setTitle("Are you sure?")
            setMessage("You can cancel the operation")
            setPositiveButton("Yes") {_, _ ->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view!!).navigate(action)
                }
            }
            setNegativeButton("Cancel") { d, _ ->
                d.cancel()
            }
        }.create().show()
    }
}
