package jpa_tdl.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the todoitem database table.
 * 
 */
@Entity
@NamedQuery(name="Todoitem.findAll", query="SELECT t FROM Todoitem t")
public class Todoitem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_item")
	private int idItem;

	@Temporal(TemporalType.DATE)
	private Date deadline;

	private String message;

	private String title;

	//bi-directional many-to-one association to Todolist
	@ManyToOne
	@JoinColumn(name="id_list")
	private Todolist todolist;

	public Todoitem() {
	}

	public Integer getIdItem() {
		return this.idItem;
	}

	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}

	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Todolist getTodolist() {
		return this.todolist;
	}

	public void setTodolist(Todolist todolist) {
		this.todolist = todolist;
	}

}