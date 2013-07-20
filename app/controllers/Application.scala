package controllers

import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.data._
import play.api.data.Forms._
import models.Task
import anorm.{NotAssigned, Pk}

object Application extends Controller {

  val taskForm = Form(
    mapping(
    "id" -> ignored(NotAssigned: Pk[Int]),
    "label" -> nonEmptyText,
    "description" -> nonEmptyText(minLength = 20)
    )(Task.apply)(Task.unapply)
  )

  def index = Action {
    Redirect(routes.Application.tasks)
  }

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      task => {
        Task.create(task)
        Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Int) = Action {
    Task.delete(id);
    Redirect(routes.Application.tasks);
  }
}