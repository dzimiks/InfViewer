package models;

public class Student {
	public String ime;
	public String prezime;
	public String indeks;
	public String path;
	public String opis;
	public String imejl;
	
	public Student(String ime, String prezime, String indeks, String path, String opis, String imejl) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.indeks = indeks;
		this.path = path;
		this.opis = opis;
		this.imejl = imejl;
	}
	
	public Student(String ime, String prezime, String indeks) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.indeks = indeks;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getIndeks() {
		return indeks;
	}

	public void setIndeks(String indeks) {
		this.indeks = indeks;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getImejl() {
		return imejl;
	}

	public void setImejl(String imejl) {
		this.imejl = imejl;
	}
}
