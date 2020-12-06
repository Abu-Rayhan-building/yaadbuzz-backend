import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IComment } from 'app/shared/model/comment.model';
import { getEntities as getComments } from 'app/entities/comment/comment.reducer';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { getEntities as getUserPerDepartments } from 'app/entities/user-per-department/user-per-department.reducer';
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
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { memorialEntity, comments, userPerDepartments, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/memorial');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getComments();
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
          <h2 id="yaadmaanApp.memorial.home.createOrEditLabel">
            <Translate contentKey="yaadmaanApp.memorial.home.createOrEditLabel">Create or edit a Memorial</Translate>
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
                  <Translate contentKey="yaadmaanApp.memorial.anonymousComment">Anonymous Comment</Translate>
                </Label>
                <AvInput id="memorial-anonymousComment" type="select" className="form-control" name="anonymousCommentId">
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
                  <Translate contentKey="yaadmaanApp.memorial.notAnonymousComment">Not Anonymous Comment</Translate>
                </Label>
                <AvInput id="memorial-notAnonymousComment" type="select" className="form-control" name="notAnonymousCommentId">
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
                  <Translate contentKey="yaadmaanApp.memorial.writer">Writer</Translate>
                </Label>
                <AvInput id="memorial-writer" type="select" className="form-control" name="writerId" required>
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
                  <Translate contentKey="yaadmaanApp.memorial.recipient">Recipient</Translate>
                </Label>
                <AvInput id="memorial-recipient" type="select" className="form-control" name="recipientId" required>
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
              <Button tag={Link} id="cancel-save" to="/memorial" replace color="info">
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
  memorialEntity: storeState.memorial.entity,
  loading: storeState.memorial.loading,
  updating: storeState.memorial.updating,
  updateSuccess: storeState.memorial.updateSuccess,
});

const mapDispatchToProps = {
  getComments,
  getUserPerDepartments,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemorialUpdate);
