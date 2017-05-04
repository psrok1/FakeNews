import * as React from "react";
import { Modal, Button } from "react-bootstrap";
import { FieldGroup } from "./FieldGroup";

export interface PushArticleModalProps {
    isVisible: boolean;
}

export interface PushArticleModalState {
    articleHeading?: string;
    articleBody?: string;
}

export class PushArticleModal extends React.Component<PushArticleModalProps, PushArticleModalState> {
    constructor(props: PushArticleModalProps)
    {
        super(props);
        this.state = {};
    }

    private onFieldChange(fieldName: string, fieldValue: string) {
        this.setState({[fieldName]: fieldValue})
    }

    render() {
        return <Modal show={this.props.isVisible} onHide={() => {}}>
            <Modal.Header>
                <Modal.Title>Add an article</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <form>
                    <FieldGroup id="articleHeading"
                                type="text"
                                label="Article heading"
                                placeholder="Article heading"
                                onChange={(ev) => {
                                    this.onFieldChange("articleHeading", (ev as any).target.value);
                                }}/>
                    <FieldGroup id="articleBody"
                                componentClass="textarea"
                                label="Article body"
                                placeholder="Article body"
                                onChange={(ev) => {
                                    this.onFieldChange("articleBody", (ev as any).target.value);
                                }}/>
                </form>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={() => {}
                }>Ok</Button>
            </Modal.Footer>
        </Modal>
    }
}