import * as React from 'react';
import { FormGroup, ControlLabel, FormControl, HelpBlock, FormControlProps } from 'react-bootstrap';

export interface FieldGroupProps extends FormControlProps {
    id: string;
    label: string;
}

export class FieldGroup extends React.Component<FieldGroupProps, undefined>
{
    render()
    {
        return (
            <FormGroup controlId={this.props.id}>
                <ControlLabel>{this.props.label}</ControlLabel>
                <FormControl {...this.props } />
            </FormGroup>
        )
    }
}