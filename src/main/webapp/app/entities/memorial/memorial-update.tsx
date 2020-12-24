import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IComment } from 'app/shared/model/comment.model';
import { getEntities as getComments } from 'app/entities/comment/comment.reducer';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { getEntities as getUserPerDepartments } from 'app/entities/user-per-department/user-per-department.reducer';
import { IDepartment } from 'app/shared/model/department.model';
import { getEntities as getDepartments } from 'app/entities/department/department.reducer';
import { getEntity, updateEntity, createEntity, reset } from './memorial.reducer';
import { IMemorial } from 'app/shared/model/memorial.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMemorialUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MemorialUpdate = (props: IMemorialUpdateProps) => {
  const [anonymousCommentId, setAnonymousCommentId] = useState('0');
  const [notAnonymousCommentId, setNotAnonymousCommentId] = useState('0');
  const [writerId, setWriterId] = useState('0');
  const [recipientId, setRecipientId] = useState('0');
  const [departmentId, setDepartmentId] = useState('0');
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { memorialEntity, comments, userPerDepartments, departments, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/memorial');
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
        ...memorialEntity,
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
          <h2 id="yaadbuzzApp.memorial.home.createOrEditLabel" data-cy="MemorialCreateUpdateHeading">
            <Translate contentKey="yaadbuzzApp.memorial.home.createOrEditLabel">Create or edit a Memorial</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : memorialEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="memorial-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="memorial-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label for="memorial-anonymousComment">
                  <Translate contentKey="yaadbuzzApp.memorial.anonymousComment">Anonymous Comment</Translate>
                </Label>
                <AvInput
                  id="memorial-anonymousComment"
                  data-cy="anonymousComment"
                  type="select"
                  className="form-control"
                  name="anonymousComment.id"
                >
                  <option value="" key="0" />
                  {comments
                    ? comments.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="memorial-notAnonymousComment">
                  <Translate contentKey="yaadbuzzApp.memorial.notAnonymousComment">Not Anonymous Comment</Translate>
                </Label>
                <AvInput
                  id="memorial-notAnonymousComment"
                  data-cy="notAnonymousComment"
                  type="select"
                  className="form-control"
                  name="notAnonymousComment.id"
                >
                  <option value="" key="0" />
                  {comments
                    ? comments.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="memorial-writer">
                  <Translate contentKey="yaadbuzzApp.memorial.writer">Writer</Translate>
                </Label>
                <AvInput id="memorial-writer" data-cy="writer" type="select" className="form-control" name="writerId" required>
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
                <Label for="memorial-recipient">
                  <Translate contentKey="yaadbuzzApp.memorial.recipient">Recipient</Translate>
                </Label>
                <AvInput id="memorial-recipient" data-cy="recipient" type="select" className="form-control" name="recipientId" required>
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
                <Label for="memorial-department">
                  <Translate contentKey="yaadbuzzApp.memorial.department">Department</Translate>
                </Label>
                <AvInput id="memorial-department" data-cy="department" type="select" className="form-control" name="departmentId" required>
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
              <Button tag={Link} id="cancel-save" to="/memorial" replace color="info">
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
  comments: storeState.comment.entities,
  userPerDepartments: storeState.userPerDepartment.entities,
  departments: storeState.department.entities,
  memorialEntity: storeState.memorial.entity,
  loading: storeState.memorial.loading,
  updating: storeState.memorial.updating,
  updateSuccess: storeState.memorial.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(MemorialUpdate);
