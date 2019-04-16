package com.rs.game.player.content;

import java.io.Serializable;

import com.rs.game.player.Player;

public final class Notes implements Serializable {

	private static final long serialVersionUID = 5564620907978487391L;
	private transient Player player;
	private transient int selectedNote = -1;

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void unlock() {
		player.getPackets().sendUnlockIComponentOptionSlots(34, 9, 0, 30, 0, 1, 2);
		player.getPackets().sendIComponentSettings(34, 9, 0, 30, 2621470);
		player.getPackets().sendHideIComponent(34, 3, false);
		player.getPackets().sendHideIComponent(34, 44, false);
		for (int i = 10; i < 16; i++)
			player.getPackets().sendHideIComponent(34, i, true);
		player.getPackets().sendConfig(1439, -1);
		fullRefresh();
	}

	public void selectNote(int slotId) {
		if (selectedNote != slotId) {
			player.getPackets().sendConfig(1439, slotId);
			selectedNote = slotId;
		} else {
			player.getPackets().sendConfig(1439, -1);
			selectedNote = -1;
		}
	}

	public int getSelectedNote() {
		return selectedNote;
	}

	public void fullRefresh() {
		try {
			for (int i = 1430; i < 1450; i++)
				player.getPackets().sendConfig(i, i);
			Note note;
			for (int i = 0; i < 30; i++) {
				if (i < player.getCurrentNotes().size()) {
					note = player.getCurrentNotes().get(i);
					player.getPackets().sendGlobalString((i + 149), note.text);
				} else {
					player.getPackets().sendGlobalString(149 + i, "");
				}
			}
			player.getPackets().sendConfig(1440, getPrimaryColour(this));
			player.getPackets().sendConfig(1441, getSecondaryColour(this));
			selectedNote = -1;
		} catch (Exception e) {
		}
	}

	public void changeColour(int componentId) {
		Note note = player.getCurrentNotes().get((int) player.getTemporaryAttributtes().get("selectednote"));
		int color = (componentId - 35) / 2;
		player.getPackets().sendHideIComponent(34, 16, true);
		note.setColour(color);
		player.getNotes().refresh(note);
	}

	public boolean refresh() {
		Note note = (Note) player.getTemporaryAttributtes().get("noteToEdit");
		if (note == null) {
			return false;
		}
		player.getPackets().sendGlobalString(149 + player.getCurrentNotes().indexOf(note), note.text);
		player.getPackets().sendConfig(1440, getPrimaryColour(this));
		player.getPackets().sendConfig(1441, getSecondaryColour(this));
		return true;
	}

	public boolean refresh(Note note) {
		if (note == null)
			return false;
		player.getPackets().sendGlobalString(149 + player.getCurrentNotes().indexOf(note), note.text);
		player.getPackets().sendConfig(1440, getPrimaryColour(this));
		player.getPackets().sendConfig(1441, getSecondaryColour(this));
		return true;
	}

	public boolean add(String value) {
		Note note = new Note(value, 0);
		if (player.getCurrentNotes().size() >= 30) {
			player.getPackets().sendGameMessage("You may only have 30 notes!", true);
			return false;
		}
		if (note.text.length() > 50) {
			player.getPackets().sendGameMessage("You can only enter notes up to 50 characters!", true);
			return false;
		}
		int id = player.getCurrentNotes().size();
		player.getPackets().sendGlobalString(149 + id, note.text);
		player.getPackets().sendConfig(1440, 0);
		player.getPackets().sendConfig(1439, 0);
		player.getCurrentNotes().add(note);
		fullRefresh();
		return false;
	}

	public boolean remove(int slotId) {
		try {
			Note toRemove = player.getCurrentNotes().get(slotId);
			if (toRemove != null)
				player.getCurrentNotes().remove(toRemove);
			player.getPackets().sendConfig(1439, 0);
			fullRefresh();
			return true;
		} catch (Exception e) {
			fullRefresh();
			return false;
		}
	}

	public void deleteAllNotes() {
		for (int i = player.getCurrentNotes().size() - 1; i >= 0; i--)
			remove(i);
	}

	public int getPrimaryColour(Notes notes) {
		int color = 0;
		for (int i = 0; i < 15; i++) {
			if (notes.player.getCurrentNotes().size() > (i)) {
				color += colourize(notes.player.getCurrentNotes().get(i).colour, i);
			}
		}
		return color;
	}

	public static int getSecondaryColour(Notes notes) {
		int color = 0;
		for (int i = 0; i < 15; i++) {
			if (notes.player.getCurrentNotes().size() > (i + 16)) {
				color += colourize(notes.player.getCurrentNotes().get(i + 16).colour, i);
			}
		}
		return color;
	}

	public static int colourize(int colour, int noteId) {
		return (int) (Math.pow(4, noteId) * colour);
	}

	public static final class Note implements Serializable {

		private static final long serialVersionUID = 9173992500345447484L;
		private String text;
		private int colour;

		public Note(String text, int colour) {
			this.text = text;
			this.colour = colour;
		}

		public String getText() {
			return text;
		}

		public int getColour() {
			return colour;
		}

		public void setText(String text) {
			this.text = text;
		}

		public void setColour(int colour) {
			this.colour = colour;
		}
	}
}