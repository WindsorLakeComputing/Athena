import * as React from "react"
import {MemoryRouter} from "react-router"
import sinon from "sinon"
import BackButton from "../components/BackButton/BackButton"
import {expect, fireEvent, render} from "../testHelpers/importsHelper"

describe("Back Button", () => {
  it("calls the browser back functionality on click", async () => {
    const backSpy = sinon.spy()
    window.history.back = backSpy

    const container = render(
      <MemoryRouter>
        <BackButton/>
      </MemoryRouter>).container

    const backButton = container.querySelector(".back-button")
    await fireEvent.click(backButton!)

    expect(backSpy).to.have.been.called
  })
})
