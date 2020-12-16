import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './memorial.reducer';
import { IMemorial } from 'app/shared/model/memorial.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMemorialDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MemorialDetail = (props: IMemorialDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { memorialEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="yaadbuzzApp.memorial.detail.title">Memorial</Translate> [<b>{memorialEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="yaadbuzzApp.memorial.anonymousComment">Anonymous Comment</Translate>
          </dt>
          <dd>{memorialEntity.anonymousCommentId ? memorialEntity.anonymousCommentId : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memorial.notAnonymousComment">Not Anonymous Comment</Translate>
          </dt>
          <dd>{memorialEntity.notAnonymousCommentId ? memorialEntity.notAnonymousCommentId : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memorial.writer">Writer</Translate>
          </dt>
          <dd>{memorialEntity.writerId ? memorialEntity.writerId : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memorial.recipient">Recipient</Translate>
          </dt>
          <dd>{memorialEntity.recipientId ? memorialEntity.recipientId : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memorial.department">Department</Translate>
          </dt>
          <dd>{memorialEntity.departmentId ? memorialEntity.departmentId : ''}</dd>
        </dl>
        <Button tag={Link} to="/memorial" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/memorial/${memorialEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ memorial }: IRootState) => ({
  memorialEntity: memorial.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemorialDetail);
