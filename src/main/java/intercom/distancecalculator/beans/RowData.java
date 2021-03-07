package intercom.distancecalculator.beans;

public class RowData {

	private int id = 0;
	private String name = null;
	private Distance distanceDetails = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Distance getDistanceDetails() {
		return distanceDetails;
	}

	public void setDistanceDetails(Distance distanceDetails) {
		this.distanceDetails = distanceDetails;
	}

	@Override
	public String toString() {
		return "RowData [id=" + id + ", name=" + name + ", distanceDetails=" + distanceDetails + "]";
	}

	public RowData(int id, String name, Double latitude, Double longitude) {
		this.id = id;
		this.name = name;
		this.distanceDetails = new Distance(latitude, longitude);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((distanceDetails == null) ? 0 : distanceDetails.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RowData other = (RowData) obj;
		if (distanceDetails == null) {
			if (other.distanceDetails != null)
				return false;
		} else if (!distanceDetails.equals(other.distanceDetails))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
