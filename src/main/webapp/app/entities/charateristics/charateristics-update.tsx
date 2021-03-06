import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { getEntities as getUserPerDepartments } from 'app/entities/user-per-department/user-per-department.reducer';
import { getEntity, updateEntity, createEntity, reset } from './charateristics.reducer';
import { ICharateristics } from 'app/shared/model/charateristics.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICharateristicsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CharateristicsUpdate = (props: ICharateristicsUpdateProps) => {
  const [userPerDepartmentId, setUserPerDepartmentId] = useState('0');
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { charateristicsEntity, userPerDepartments, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/charateristics');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getUserPerDepartments();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...charateristicsEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="yaadbuzzApp.charateristics.home.createOrEditLabel" data-cy="CharateristicsCreateUpdateHeading">
            <Translate contentKey="yaadbuzzApp.charateristics.home.createOrEditLabel">Create or edit a Charateristics</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : charateristicsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="charateristics-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="charateristics-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="titleLabel" for="charateristics-title">
                  <Translate contentKey="yaadbuzzApp.charateristics.title">Title</Translate>
                </Label>
                <AvField
                  id="charateristics-title"
                  data-cy="title"
                  type="text"
                  name="title"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="repetationLabel" for="charateristics-repetation">
                  <Translate contentKey="yaadbuzzApp.charateristics.repetation">Repetation</Translate>
                </Label>
                <AvField id="charateristics-repetation" data-cy="repetation" type="string" className="form-control" name="repetation" />
              </AvGroup>
              <AvGroup>
                <Label for="charateristics-userPerDepartment">
                  <Translate contentKey="yaadbuzzApp.charateristics.userPerDepartment">User Per Department</Translate>
                </Label>
                <AvInput
                  id="charateristics-userPerDepartment"
                  data-cy="userPerDepartment"
                  type="select"
                  className="form-control"
                  name="userPerDepartmentId"
                  required
                >
                  {userPerDepartments
                    ? userPerDepartments.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/charateristics" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  userPerDepartments: storeState.userPerDepartment.entities,
  charateristicsEntity: storeState.charateristics.entity,
  loading: storeState.charateristics.loading,
  updating: storeState.charateristics.updating,
  updateSuccess: storeState.charateristics.updateSuccess,
});

const mapDispatchToProps = {
  getUserPerDepartments,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CharateristicsUpdate);
