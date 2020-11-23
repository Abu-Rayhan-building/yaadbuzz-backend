import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { getEntities as getUserPerDepartments } from 'app/entities/user-per-department/user-per-department.reducer';
import { ICharateristics } from 'app/shared/model/charateristics.model';
import { getEntities as getCharateristics } from 'app/entities/charateristics/charateristics.reducer';
import { getEntity, updateEntity, createEntity, reset } from './charateristics-repetation.reducer';
import { ICharateristicsRepetation } from 'app/shared/model/charateristics-repetation.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICharateristicsRepetationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CharateristicsRepetationUpdate = (props: ICharateristicsRepetationUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [charactristicId, setCharactristicId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { charateristicsRepetationEntity, userPerDepartments, charateristics, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/charateristics-repetation');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUserPerDepartments();
    props.getCharateristics();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...charateristicsRepetationEntity,
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
          <h2 id="yaadmaanApp.charateristicsRepetation.home.createOrEditLabel">
            <Translate contentKey="yaadmaanApp.charateristicsRepetation.home.createOrEditLabel">
              Create or edit a CharateristicsRepetation
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : charateristicsRepetationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="charateristics-repetation-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="charateristics-repetation-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="repetationLabel" for="charateristics-repetation-repetation">
                  <Translate contentKey="yaadmaanApp.charateristicsRepetation.repetation">Repetation</Translate>
                </Label>
                <AvField id="charateristics-repetation-repetation" type="string" className="form-control" name="repetation" />
              </AvGroup>
              <AvGroup>
                <Label for="charateristics-repetation-user">
                  <Translate contentKey="yaadmaanApp.charateristicsRepetation.user">User</Translate>
                </Label>
                <AvInput id="charateristics-repetation-user" type="select" className="form-control" name="userId" required>
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
              <AvGroup>
                <Label for="charateristics-repetation-charactristic">
                  <Translate contentKey="yaadmaanApp.charateristicsRepetation.charactristic">Charactristic</Translate>
                </Label>
                <AvInput
                  id="charateristics-repetation-charactristic"
                  type="select"
                  className="form-control"
                  name="charactristicId"
                  required
                >
                  {charateristics
                    ? charateristics.map(otherEntity => (
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
              <Button tag={Link} id="cancel-save" to="/charateristics-repetation" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
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
  charateristics: storeState.charateristics.entities,
  charateristicsRepetationEntity: storeState.charateristicsRepetation.entity,
  loading: storeState.charateristicsRepetation.loading,
  updating: storeState.charateristicsRepetation.updating,
  updateSuccess: storeState.charateristicsRepetation.updateSuccess,
});

const mapDispatchToProps = {
  getUserPerDepartments,
  getCharateristics,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CharateristicsRepetationUpdate);
