package com.example.demo.utill;

import java.util.List;

public class Pager<T> {
	private int page;
	private int size;
	private List<T> data;
	private long total;

	public Pager(int page, int size, List<T> data, long total) {

		if (page < 1) {
			throw new IllegalArgumentException("Page number must be greater than or equal to 1.");
		}
		if (size <= 0) {
			throw new IllegalArgumentException("Page size must be greater than 0.");
		}

		this.page = page;
		this.size = size;
		this.data = data;
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page < 1) {
			throw new IllegalArgumentException("Page number must be greater than or equal to 1.");
		}
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Page size must be greater than 0.");
		}
		this.size = size;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Pager{" + "page=" + page + ", size=" + size + ", data=" + data + ", total=" + total + '}';
	}
}
