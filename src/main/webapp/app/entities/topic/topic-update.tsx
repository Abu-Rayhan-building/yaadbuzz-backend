import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDepartment } from 'app/shared/model/department.model';
import { getEntities as getDepartments } from 'app/entities/department/department.reducer';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { getEntities as getUserPerDepartments } from 'app/entities/user-per-department/user-per-department.reducer';
import { getEntity, updateEntity, createEntity, reset } from './topic.reducer';
import { ITopic } from 'app/shared/model/topic.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITopicUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TopicUpdate = (props: ITopicUpdateProps) => {
  const [idsvoters, setIdsvoters] = useState([]);
  const [departmentId, setDepartmentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { topicEntity, departments, userPerDepartments, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/topic');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getDepartments();
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
        ...topicEntity,
        ...values,
        voters: mapIdList(values.voters),
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
          <h2 id="yaadmaanApp.topic.home.createOrEditLabel">
            <Translate contentKey="yaadmaanApp.topic.home.createOrEditLabel">Create or edit a Topic</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : topicEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="topic-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="topic-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="titleLabel" for="topic-title">
                  <Translate contentKey="yaadmaanApp.topic.title">Title</Translate>
                </Label>
                <AvField
                  id="topic-title"
                  type="text"
                  name="title"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="topic-department">
                  <Translate contentKey="yaadmaanApp.topic.department">Department</Translate>
                </Label>
                <AvInput id="topic-department" type="select" className="form-control" name="departmentId" required>
                  {departments
                    ? departments.map(otherEntity => (
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
                <Label for="topic-voters">
                  <Translate contentKey="yaadmaanApp.topic.voters">Voters</Translate>
                </Label>
                <AvInput
                  id="topic-voters"
                  type="select"
                  multiple
                  className="form-control"
                  name="voters"
                  value={topicEntity.voters && topicEntity.voters.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {userPerDepartments
                    ? userPerDepartments.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/topic" replace color="info">
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
  departments: storeState.department.entities,
  userPerDepartments: storeState.userPerDepartment.entities,
  topicEntity: storeState.topic.entity,
  loading: storeState.topic.loading,
  updating: storeState.topic.updating,
  updateSuccess: storeState.topic.updateSuccess,
});

const mapDispatchToProps = {
  getDepartments,
  getUserPerDepartments,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TopicUpdate);
