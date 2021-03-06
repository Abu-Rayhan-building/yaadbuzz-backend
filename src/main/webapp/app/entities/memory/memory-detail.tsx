import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './memory.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMemoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MemoryDetail = (props: IMemoryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { memoryEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="memoryDetailsHeading">
          <Translate contentKey="yaadbuzzApp.memory.detail.title">Memory</Translate> [<strong>{memoryEntity.id}</strong>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="title">
              <Translate contentKey="yaadbuzzApp.memory.title">Title</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.title}</dd>
          <dt>
            <span id="isPrivate">
              <Translate contentKey="yaadbuzzApp.memory.isPrivate">Is Private</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.isPrivate ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memory.baseComment">Base Comment</Translate>
          </dt>
          <dd>{memoryEntity.baseComment ? memoryEntity.baseComment.id : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memory.writer">Writer</Translate>
          </dt>
          <dd>{memoryEntity.writer ? memoryEntity.writer.id : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memory.taged">Taged</Translate>
          </dt>
          <dd>
            {memoryEntity.tageds
              ? memoryEntity.tageds.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {memoryEntity.tageds && i === memoryEntity.tageds.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memory.department">Department</Translate>
          </dt>
          <dd>{memoryEntity.department ? memoryEntity.department.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/memory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/memory/${memoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ memory }: IRootState) => ({
  memoryEntity: memory.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemoryDetail);
