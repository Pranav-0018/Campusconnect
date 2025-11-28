import { Authenticated, Unauthenticated } from "convex/react";
import { SignInForm } from "./SignInForm";
import { SignOutButton } from "./SignOutButton";
import Dashboard from "./components/Dashboard";
import RoleSetup from "./components/RoleSetup";
import { useQuery } from "convex/react";
import { api } from "../convex/_generated/api";

function App() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-600 to-blue-500">
      <Unauthenticated>
        <div className="flex items-center justify-center min-h-screen p-4">
          <div className="bg-white rounded-lg shadow-xl p-8 w-full max-w-md">
            <div className="text-center mb-8">
              <h1 className="text-3xl font-bold text-purple-600 mb-2">
                Campus Connect
              </h1>
              <p className="text-gray-600">Connect. Learn. Grow.</p>
            </div>
            <SignInForm />
          </div>
        </div>
      </Unauthenticated>
      
      <Authenticated>
        <AuthenticatedApp />
      </Authenticated>
    </div>
  );
}

function AuthenticatedApp() {
  const profileData = useQuery(api.profiles.get);

  if (profileData === undefined) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-purple-600"></div>
      </div>
    );
  }

  if (!profileData?.profile) {
    return <RoleSetup />;
  }

  return <Dashboard profile={profileData.profile} user={profileData.user} />;
}

export default App;
