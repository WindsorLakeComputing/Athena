import React from "react"
import Input from "../components/Input"
import {expect, fireEvent, render} from "../testHelpers/importsHelper"

describe("Input", () => {
  let expectedMessage: string
  let container: HTMLElement

  beforeEach(() => {
    expectedMessage = "error message"
    container = render(
      <Input label={"something"}
             id={"inputId"}
             error={expectedMessage}
             inputProps={{ required: true }}
             onChange={() => {
               // do nothing
             }}
      />
    ).container
  })

  describe("input is required", () => {
    it("should render error text when left blank and then blurred", async () => {
      const input = container.querySelector("#inputId")

      await fireEvent.click(input!)
      await fireEvent.blur(input!)

      const errorText = container.querySelector(".error_text")

      expect(errorText!.textContent).to.equal(expectedMessage)
    })

    it("should not display error message when input is filled out", async () => {
      const input = container.querySelector("#inputId")
      await fireEvent.change(input!, {target: {value: "Test"}})
      const errorText = container.querySelector(".error_text")

      expect(errorText).to.be.null
    })

    it("should not display error message when input is left blank, blurred, and then filled out", async () => {
      const input = container.querySelector("#inputId")
      await fireEvent.click(input!)
      await fireEvent.blur(input!)
      await fireEvent.change(input!, {target: {value: "Test"}})
      const errorText = container.querySelector(".error_text")

      expect(errorText).to.be.null
    })
  })
})
