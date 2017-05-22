import * as React from "react";
import { Modal, Button } from "react-bootstrap";
import { FieldGroup } from "./FieldGroup";

export interface ArticleModalProps {
    isVisible: boolean;
    title: string;
    articleHeading: string;
    articleBody: string;
    articleOrigin?: string;
    articlePublished?: string;
    readonly?: boolean;
    onChange?: React.EventHandler<React.FormEvent<React.Component<ReactBootstrap.FormControlProps, {}>>>;
    onAccept: () => void;
    onCancel: () => void;
}

export class ArticleModal extends React.Component<ArticleModalProps, undefined> {
    render() {
        var additionalFields: JSX.Element[] = [];

        if(this.props.articleOrigin)
        {
            additionalFields = additionalFields.concat([
                    <dt><strong>Original article</strong></dt>,
                    <dd><a href={this.props.articleOrigin} target="_blank">{this.props.articleHeading}</a></dd>
            ]);
        }

        if(this.props.articlePublished)
        {
            additionalFields = additionalFields.concat([
                    <dt><strong>Published on</strong></dt>,
                    <dd>{this.props.articlePublished}</dd>
            ]);
        }

        return <Modal show={this.props.isVisible} onHide={this.props.onCancel}>
            <Modal.Header>
                <Modal.Title>{this.props.title}</Modal.Title>
                <dl>
                    {additionalFields}
                </dl>
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