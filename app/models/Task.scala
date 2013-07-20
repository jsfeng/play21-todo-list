package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current


case class Task( id:Pk[Int], label:String, description:String)

object Task {

  val task = {
      get[Pk[Int]]("id") ~
      get[String]("label") ~
      get[String]("description") map {
        case id~label~description => Task(id, label,description)
    }
  }

  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }

  def create(task:Task) {
    DB.withConnection { implicit c =>
      SQL("insert into task (label,description) values ({label},{description})").on(
        'label -> task.label,
        'description -> task.description
      ).executeUpdate()
    }
  }

  def delete(id: Int) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }
}