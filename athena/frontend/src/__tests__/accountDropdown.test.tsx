import React from "react"
import {MemoryRouter} from "react-router"
import {RenderResult} from "react-testing-library"
import AccountDropdown from "../components/AccountDropdown"
import {expect, fireEvent, render, sinon} from "../testHelpers/importsHelper"

describe("accountDropdown", () => {
  let container: HTMLElement
  let rendered: RenderResult

  beforeEach(() => {
    rendered = render(
      <MemoryRouter>
        <AccountDropdown/>
      </MemoryRouter>
    )
    container = rendered.container
  })

  it("click displays the dropdown", async () => {
    const logout = container.querySelectorAll(".dropdown-link")[0]
    await fireEvent.click(logout)

    expect(container.querySelectorAll("#add-user-button").length).to.eq(1)
    expect(container.querySelectorAll("#logout-button").length).to.eq(1)
  })

  it("Submits a form to /logout on logout click", async () => {
    const form = (document.querySelector("#logout-form") as HTMLFormElement)
    form.submit = sinon.stub()

    const logout = container.querySelectorAll(".dropdown-link")[0]
    await fireEvent.click(logout)

    const logoutButton = container.querySelectorAll("#logout-button")[0]
    await fireEvent.click(logoutButton)

    expect(form.action).to.equal("http://localhost/logout")
    expect(form.method).to.equal("post")
    expect(form.submit).to.have.been.called
  })
})
