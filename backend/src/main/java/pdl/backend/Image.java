package pdl.backend;

public class Image {
  private static Long count = Long.valueOf(0);
  private Long id;
  private String name;
  private byte[] data;
  private String type; // ajout de la propriété "type"


  public Image(final String name, final byte[] data) {
    id = count++;
    this.name = name;
    this.data = data;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public byte[] getData() {
    return data;
  }
  public String getType() { // getter pour la propriété "type"
  return type;
}

public void setType(String type) { // setter pour la propriété "type"
  this.type = type;
}
}
