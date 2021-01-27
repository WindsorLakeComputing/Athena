import React from "react"
import {MemoryRouter} from "react-router"
import NavigationBar from "../components/NavigationBar"
import {expect, render} from "../testHelpers/importsHelper"

describe("navigationBar", () => {
  let container: HTMLElement

  const defaultProps = {
    userName: "test",
    getMaintainerList: () => null
  }

  it("displays account dropdown button", () => {
    container = render(
      <MemoryRouter>
        <NavigationBar {...defaultProps}/>
      </MemoryRouter>
    ).container

    expect(container.querySelectorAll(".dropdown-link").length).to.eq(1)
  })

  describe("On Schedule View", () => {
    it("displays an upload button", () => {
      container = render(
        <MemoryRouter initialEntries={["/"]}>
          <NavigationBar {...defaultProps}/>
        </MemoryRouter>).container

      expect(container.querySelector(".upload-button")).to.exist
    })
  })

  describe("On Maintainer Detail", () => {
    it("Doesn't show an upload button", () => {
      container = render(
        <MemoryRouter initialEntries={["/maintainer/1"]}>
          <NavigationBar {...defaultProps}/>
        </MemoryRouter>).container

      expect(container.querySelectorAll(".upload-button").length).to.equal(0)
    })
  })
})
