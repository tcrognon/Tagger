package edu.utsa.tagger;

import java.util.UUID;

public class DataWrappersSelectedTag implements Comparable<DataWrappersSelectedTag> {

	private UUID item_uuid;
	private UUID tag_uuid;
	
	public DataWrappersSelectedTag(UUID item_uuid, UUID tag_uuid) {
		if (item_uuid == null || tag_uuid == null) {
			throw new NullPointerException();
		}
		else {
			this.item_uuid = item_uuid;
			this.tag_uuid = tag_uuid;
		}
	}
	
	public UUID getItemUuid() {
		return item_uuid;
	}
	
	public UUID getTagUuid() {
		return tag_uuid;
	}
	
	@Override public int compareTo(DataWrappersSelectedTag selected_tag) {
		if (selected_tag == null) {
			throw new NullPointerException();
		}
		else {
			int result = this.item_uuid.compareTo(selected_tag.item_uuid);
			if (result == 0) {
				result = this.tag_uuid.compareTo(selected_tag.tag_uuid);
			}
			return result;
		}
	}

	@Override public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DataWrappersSelectedTag)) {
			return false;
		}
		else {
			return (this.compareTo((DataWrappersSelectedTag) obj) == 0);
		}
	}
}
