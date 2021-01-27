import React, {Component} from "react"

interface State {
  isValid: boolean
}

interface Props {
  label: string
  onChange: (event: any) => void
  id: string
  inputProps?: React.InputHTMLAttributes<HTMLInputElement>
  error?: string
}

interface DefaultProps {
  error: string
}

class Input extends Component<Props, State> {
  private static defaultProps: DefaultProps = {
    error: ""
  }

  constructor(props: any) {
    super(props)
    this.state = {
      isValid: true
    }
  }

  public componentDidMount(): void {
    const input: HTMLInputElement | null = document.querySelector(`#${this.props.id}`)
    if (input) {
      input!.addEventListener("invalid", (event) => {
        input!.classList.add("error")
        this.setState({isValid: false})
      }, false)

      input!.addEventListener("blur", (event) => {
        input!.checkValidity()
        this.setState({isValid: input.validity.valid})
      })
    }
  }

  public render() {
    const {label, id} = this.props
    return (<div className={"input_container"}>
      <label htmlFor={id}>{label}</label>
      <input
        {...this.props.inputProps}
        id={id}
        className="text_input"
        onChange={(e) => {
          this.props.onChange(e)
          const valid = e.target.validity.valid
          this.setState({isValid: valid})
        }}
      />
      {this.state.isValid
        ?
          <div className={"error_text_placeholder"}/>
        :
          <div className={"error_text"}>
            {this.props.error}
          </div>
      }
    </div>)
  }
}

export default Input
