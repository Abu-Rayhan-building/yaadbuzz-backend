import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IComment } from 'app/shared/model/comment.model';
import { getEntities as getComments } from 'app/entities/comment/comment.reducer';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { getEntities as getUserPerDepartments } from 'app/entities/user-per-department/user-per-department.reducer';
import { IDepartment } from 'app/shared/model/department.model';
import { getEntities as getDepartments } from 'app/entities/department/department.reducer';
import { getEntity, updateEntity, createEntity, reset } from './memory.reducer';
import { IMemory } from 'app/shared/model/memory.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMemoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MemoryUpdate = (props: IMemoryUpdateProps) => {
  const [idstaged, setIdstaged] = useState([]);
  const [commentsId, setCommentsId] = useState('0');
  const [textId, setTextId] = useState('0');
  const [writerId, setWriterId] = useState('0');
  const [departmentId, setDepartmentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { memoryEntity, comments, userPerDepartments, departments, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/memory');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getComments();
    props.getUserPerDepartments();
    props.getDepartments();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...memoryEntity,
        ...values,
        tageds: mapIdList(values.tageds),
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
          <h2 id="yaadmaanApp.memory.home.createOrEditLabel">
            <Translate contentKey="yaadmaanApp.memory.home.createOrEditLabel">Create or edit a Memory</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : memoryEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="memory-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="memory-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="titleLabel" for="memory-title">
                  <Translate contentKey="yaadmaanApp.memory.title">Title</Translate>
                </Label>
                <AvField id="memory-title" type="text" name="title" />
              </AvGroup>
              <AvGroup check>
                <Label id="isPrivateLabel">
                  <AvInput id="memory-isPrivate" type="checkbox" className="form-check-input" name="isPrivate" />
                  <Translate contentKey="yaadmaanApp.memory.isPrivate">Is Private</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="memory-text">
                  <Translate contentKey="yaadmaanApp.memory.text">Text</Translate>
                </Label>
                <AvInput id="memory-text" type="select" className="form-control" name="textId" required>
                  {comments
                    ? comments.map(otherEntity => (
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
                <Label for="memory-writer">
                  <Translate contentKey="yaadmaanApp.memory.writer">Writer</Translate>
                </Label>
                <AvInput id="memory-writer" type="select" className="form-control" name="writerId" required>
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
                <Label for="memory-taged">
                  <Translate contentKey="yaadmaanApp.memory.taged">Taged</Translate>
                </Label>
                <AvInput
                  id="memory-taged"
                  type="select"
                  multiple
                  className="form-control"
                  name="tageds"
                  value={memoryEntity.tageds && memoryEntity.tageds.map(e => e.id)}
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
              <AvGroup>
                <Label for="memory-department">
                  <Translate contentKey="yaadmaanApp.memory.department">Department</Translate>
                </Label>
                <AvInput id="memory-department" type="select" className="form-control" name="departmentId" required>
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
              <Button tag={Link} id="cancel-save" to="/memory" replace color="info">
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
  comments: storeState.comment.entities,
  userPerDepartments: storeState.userPerDepartment.entities,
  departments: storeState.department.entities,
  memoryEntity: storeState.memory.entity,
  loading: storeState.memory.loading,
  updating: storeState.memory.updating,
  updateSuccess: storeState.memory.updateSuccess,
});

const mapDispatchToProps = {
  getComments,
  getUserPerDepartments,
  getDepartments,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemoryUpdate);
