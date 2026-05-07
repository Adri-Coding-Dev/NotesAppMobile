package com.noteapp.domain.usecase;

import com.noteapp.domain.repository.NoteRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class GetAllNotesUseCase_Factory implements Factory<GetAllNotesUseCase> {
  private final Provider<NoteRepository> repositoryProvider;

  public GetAllNotesUseCase_Factory(Provider<NoteRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAllNotesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAllNotesUseCase_Factory create(Provider<NoteRepository> repositoryProvider) {
    return new GetAllNotesUseCase_Factory(repositoryProvider);
  }

  public static GetAllNotesUseCase newInstance(NoteRepository repository) {
    return new GetAllNotesUseCase(repository);
  }
}
