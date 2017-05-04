import * as React from "react";
import { Modal, Button } from "react-bootstrap";
import { FieldGroup } from "./FieldGroup";

export interface ArticleModalProps {
    isVisible: boolean;
    title: string;
    articleHeading: string;
    articleBody: string;
    readonly?: boolean;
    onChange?: React.EventHandler<React.FormEvent<React.Component<ReactBootstrap.FormControlProps, {}>>>;
    onAccept: () => void;
    onCancel: () => void;
}

export class ArticleModal extends React.Component<ArticleModalProps, undefined> {
    render() {
        return <Modal show={this.props.isVisible} onHide={this.props.onCancel}>
            <Modal.Header>
                <Modal.Title>{this.props.title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <form>
                    <FieldGroup id="articleHeading"
                                type="text"
                                label="Article heading"
                                placeholder="Article heading"
                                value={this.props.articleHeading}
                                readOnly={this.props.readonly}
                                onChange={this.props.onChange}/>
                    <FieldGroup id="articleBody"
                                componentClass="textarea"
                                label="Article body"
                                placeholder="Article body"
                                value={this.props.articleBody}
                                readOnly={this.props.readonly}
                                onChange={this.props.onChange}/>
                </form>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={this.props.onAccept}>Ok</Button>
                <Button onClick={this.props.onCancel}>Cancel</Button>
            </Modal.Footer>
        </Modal>
    }
}