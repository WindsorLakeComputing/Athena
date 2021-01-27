import React, {Component, FormEvent} from "react"

interface Props {
  onSubmit: any
  className: string
  autoComplete: string
}

export class AthenaForm extends Component<Props> {

  constructor(props: any) {
    super(props)
  }

  public componentDidMount(): void {
    const inputs = document.querySelectorAll("input")

    for (const input of inputs as any) {
      // Just before submit, the invalid event will fire, let's apply our class there.
      input.addEventListener("invalid", (event: any) => {
        event.preventDefault()
        input.classList.add("error")
      }, false)
    }
  }

  public render() {
    return (
      <form className={this.props.className}
            autoComplete={this.props.autoComplete}
            onSubmit={this.onSubmit}
            >
        {this.props.children}
      </form>
    )
  }

  private onSubmit = () =>  {
    this.props.onSubmit()
    const inputs = document.querySelectorAll("input")

    for (const input of inputs as any) {
        input.classList.remove("error")
    }
  }
}
