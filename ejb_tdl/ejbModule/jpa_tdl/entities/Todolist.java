package jpa_tdl.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the todolist database table.
 * 
 */
@Entity
@NamedQuery(name="Todolist.findAll", query="SELECT t FROM Todolist t")
public class Todolist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_list")
	private int idList;

	@Temporal(TemporalType.DATE)
	private Date date;

	private String title;

	//bi-directional many-to-one association to Todoitem
	@OneToMany(mappedBy="todolist")
	private List<Todoitem> todoitems;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="id_user")
	private User user;

	public Todolist() {
	}

	public Integer getIdList() {
		return this.idList;
	}

	public void setIdList(int idList) {
		this.idList = idList;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Todoitem> getTodoitems() {
		return this.todoitems;
	}

	public void setTodoitems(List<Todoitem> todoitems) {
		this.todoitems = todoitems;
	}

	public Todoitem addTodoitem(Todoitem todoitem) {
		getTodoitems().add(todoitem);
		todoitem.setTodolist(this);

		return todoitem;
	}

	public Todoitem removeTodoitem(Todoitem todoitem) {
		getTodoitems().remove(todoitem);
		todoitem.setTodolist(null);

		return todoitem;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}