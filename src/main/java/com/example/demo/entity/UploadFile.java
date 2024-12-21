package com.example.demo.entity;

public class UploadFile {

	private Long id; // 使用 Long 类型作为主键，自动增长
	private String name;
	private String url;
	private int isPublic;

	@Override
	public String toString() {
		return "UploadFile [id=" + id + ", name=" + name + ", url=" + url + ", isPublic=" + isPublic + "]";
	}

	// 构造方法
	public UploadFile(Long id, String name, String url, int isPublic) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.isPublic = isPublic;
	}

	public UploadFile() {
	}

	// Getter 和 Setter 方法
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(int isPublic) {
		this.isPublic = isPublic;
	}
}
