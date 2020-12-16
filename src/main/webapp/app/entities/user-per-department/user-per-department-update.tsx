import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPicture } from 'app/shared/model/picture.model';
import { getEntities as getPictures } from 'app/entities/picture/picture.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IDepartment } from 'app/shared/model/department.model';
import { getEntities as getDepartments } from 'app/entities/department/department.reducer';
import { ITopic } from 'app/shared/model/topic.model';
import { getEntities as getTopics } from 'app/entities/topic/topic.reducer';
import { IMemory } from 'app/shared/model/memory.model';
import { getEntities as getMemories } from 'app/entities/memory/memory.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-per-department.reducer';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserPerDepartmentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UserPerDepartmentUpdate = (props: IUserPerDepartmentUpdateProps) => {
  const [avatarId, setAvatarId] = useState('0');
  const [realUserId, setRealUserId] = useState('0');
  const [departmentId, setDepartmentId] = useState('0');
  const [topicsVotedId, setTopicsVotedId] = useState('0');
  const [tagedInMemoeriesId, setTagedInMemoeriesId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { userPerDepartmentEntity, pictures, users, departments, topics, memories, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/user-per-department');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getPictures();
    props.getUsers();
    props.getDepartments();
    props.getTopics();
    props.getMemories();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...userPerDepartmentEntity,
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
          <h2 id="yaadbuzzApp.userPerDepartment.home.createOrEditLabel">
            <Translate contentKey="yaadbuzzApp.userPerDepartment.home.createOrEditLabel">Create or edit a UserPerDepartment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : userPerDepartmentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="user-per-department-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="user-per-department-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nicNameLabel" for="user-per-department-nicName">
                  <Translate contentKey="yaadbuzzApp.userPerDepartment.nicName">Nic Name</Translate>
                </Label>
                <AvField id="user-per-department-nicName" type="text" name="nicName" validate={{}} />
              </AvGroup>
              <AvGroup>
                <Label id="bioLabel" for="user-per-department-bio">
                  <Translate contentKey="yaadbuzzApp.userPerDepartment.bio">Bio</Translate>
                </Label>
                <AvField id="user-per-department-bio" type="text" name="bio" />
              </AvGroup>
              <AvGroup>
                <Label for="user-per-department-avatar">
                  <Translate contentKey="yaadbuzzApp.userPerDepartment.avatar">Avatar</Translate>
                </Label>
                <AvInput id="user-per-department-avatar" type="select" className="form-control" name="avatarId">
                  <option value="" key="0" />
                  {pictures
                    ? pictures.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="user-per-department-realUser">
                  <Translate contentKey="yaadbuzzApp.userPerDepartment.realUser">Real User</Translate>
                </Label>
                <AvInput id="user-per-department-realUser" type="select" className="form-control" name="realUserId" required>
                  {users
                    ? users.map(otherEntity => (
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
                <Label for="user-per-department-department">
                  <Translate contentKey="yaadbuzzApp.userPerDepartment.department">Department</Translate>
                </Label>
                <AvInput id="user-per-department-department" type="select" className="form-control" name="departmentId" required>
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
              <Button tag={Link} id="cancel-save" to="/user-per-department" replace color="info">
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
  pictures: storeState.picture.entities,
  users: storeState.userManagement.users,
  departments: storeState.department.entities,
  topics: storeState.topic.entities,
  memories: storeState.memory.entities,
  userPerDepartmentEntity: storeState.userPerDepartment.entity,
  loading: storeState.userPerDepartment.loading,
  updating: storeState.userPerDepartment.updating,
  updateSuccess: storeState.userPerDepartment.updateSuccess,
});

const mapDispatchToProps = {
  getPictures,
  getUsers,
  getDepartments,
  getTopics,
  getMemories,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserPerDepartmentUpdate);
